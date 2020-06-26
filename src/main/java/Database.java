import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Kursas;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.fxml.Initializable;

public class Database implements Initializable {

    private static Connection con;

        @FXML
        private DatePicker kursoData3;
        @FXML
        private AnchorPane anchorPane;
        @FXML
        private MenuItem gautiSar;
        @FXML
        private MenuItem rodytiSar;
        @FXML
        private MenuItem rodytiKur;
        @FXML
        private MenuItem rodytiErr;
        @FXML
        private MenuItem gautiKur;
        @FXML
        private MenuItem rodytiPar;
        @FXML
        private MenuItem rodytiApie;

        @FXML
        private ObservableList<Kursas> duomenys;

        @FXML
        private AnchorPane secondScene;
        @FXML
        Label dateTime;

    public Database() throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
    }
        @Override
        public void initialize(URL location, ResourceBundle resources) {

//            mywebview.getEngine().load("https://www.lb.lt/webservices/ExchangeRates/ExchangeRates.asmx?op=getExchangeRate");

                initClock();
         }


        @FXML
        public void gautiSarasaXML(ActionEvent actionEvent) {
            try {

                String uri = "https://www.lb.lt/webservices/ExchangeRates/ExchangeRates.asmx/getListOfCurrencies";

                URLConnection urlConnection = new URL(uri).openConnection();
                urlConnection.setUseCaches(false);
                urlConnection.setDoOutput(true); // Triggers POST.
                urlConnection.setRequestProperty("content-type", "application/x-www-form-urlencoded");

                OutputStreamWriter writer = null;
                try {
                    writer = new OutputStreamWriter(urlConnection.getOutputStream());
                    writer.write("POST"); // Write POST query string (if any needed).
                } finally {
                    if (writer != null) try {
                        writer.close();
                    } catch (IOException logOrIgnore) {
                    }
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));

                String line = "";
                String xmlResponse = "";
                while ((line = reader.readLine()) != null) {
                    xmlResponse += line;
                }

                SAXBuilder builder = new SAXBuilder();
                Document document = builder.build(new ByteArrayInputStream(xmlResponse.getBytes("UTF-8")));
                Element rootNode;
                rootNode = document.getRootElement();
                List list = rootNode.getChildren("item");

                //step2 create  the connection object
                con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.2.154:1521:xe", "VARTOTOJAS", "vartotojas");
                //step3 create the statement object
                Statement stmt = con.createStatement();

                //step4 execute query
                stmt.executeQuery("delete from sarasas");

                String veiksmas = "insert into sarasas (kodas,pavadinimas) values (?, ?)";
                PreparedStatement paruosta = con.prepareStatement(veiksmas);

                int list_size = list.size();
                for (int i = 0; i < list.size(); i++) {

                    Element node = (Element) list.get(i);
                    List<Element> eles = node.getChildren("description");

                    paruosta.setString(1, node.getChildText("currency"));
                    paruosta.setString(2, eles.get(1).getValue());
                    paruosta.executeUpdate();

                }
                con.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @FXML
        public void rodytiSarasa2() throws SQLException {
            Parent window1;

            try {
                window1 = FXMLLoader.load(getClass().getResource("/PilnasSarasas.fxml"));
                final Stage window1Stage = new Stage();
                window1Stage.setTitle("Visos valiutos");
                window1Stage.initModality(Modality.APPLICATION_MODAL);
                window1Stage.initOwner(ValiutuKursai2.homeStage);
                Scene window1Scene = new Scene(window1, 1258, 666);
                window1Stage.setScene(window1Scene);
                window1Stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @FXML
        public void gautiKursus2() throws SQLException {
            final Stage dialog = new Stage();
            dialog.setTitle("Kuriai datai gauti kursus");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(ValiutuKursai2.homeStage);
            DatePicker kursoData3 = new DatePicker();
            kursoData3.setPromptText("kuri data");
            Button newbutton5 = new Button("Vykdyti");
            newbutton5.setAlignment(Pos.CENTER);

            newbutton5.setOnAction(value ->  {
                try {
                    gautiKursusXML2(kursoData3);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
            VBox dialogVbox = new VBox(kursoData3,newbutton5);
            dialogVbox.setSpacing(30);
            dialogVbox.prefWidthProperty().bind(anchorPane.widthProperty());
            dialogVbox.prefHeightProperty().bind(anchorPane.heightProperty());
            dialogVbox.setAlignment(Pos.CENTER);
            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();

        }

        @FXML
        private void gautiKursusXML2(DatePicker kursoData3) throws SQLException {
            //pasimti pageidaujamu valiutu sarasa
            con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.2.154:1521:xe", "VARTOTOJAS", "vartotojas");
            String query = "SELECT kodas FROM pasirinktos";
            PreparedStatement statement = null;
            try {
                statement = con.prepareStatement(query);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            ResultSet resultSet = null;
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String kodas = resultSet.getString("kodas");
                gautiVienaKursa (kodas,kursoData3);
            }
            con.close();
        }

        @FXML
        private void gautiVienaKursa(String kodas, DatePicker kursoData3) {
            URL urlForGetRequest = null;
            String kokiaData = null;
            try {
                kokiaData = kursoData3.getValue().toString();
//                System.out.println(kokiaData);
                String koksURL = "https://www.lb.lt/webservices/FxRates/FxRates.asmx/getFxRatesForCurrency?tp=EU&ccy=" + kodas + "&dtFrom=" + kokiaData + "&dtTo=" + kokiaData;
                urlForGetRequest = new URL(koksURL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            String readLine = null;
            HttpURLConnection conection = null;
            try {
                conection = (HttpURLConnection) urlForGetRequest.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                conection.setRequestMethod("GET");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }

            int responseCode = 0;
            try {
                responseCode = conection.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = null;
                try {
                    in = new BufferedReader(
                            new InputStreamReader(conection.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                StringBuffer response = new StringBuffer();
                while (true) {
                    try {
                        if (!((readLine = in.readLine()) != null)) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    response.append(readLine);
                }
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //
                SAXBuilder builder = new SAXBuilder();
                Document document = null;
                try {
                    document = builder.build(new ByteArrayInputStream(response.toString().getBytes("UTF-8")));
                } catch (JDOMException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Element rootNode;
                rootNode = document.getRootElement();
                //  Namespace ns = Namespace.getNamespace("http://www.lb.lt/WebServices/FxRates");

                Element typeContent = rootNode.getChildren().get(0);
                if (typeContent.getName() != "OprlErr"){

                    List<Element> list = typeContent.getChildren();

                    String kas = list.get(1).getText(); //data

                    List<Element> list2 = list.get(2).getChildren(); //from curr
                    List<Element> list3 = list.get(3).getChildren(); //to curr
                    String kas2 = list2.get(0).getText(); //from curr
                    String kas3 = list2.get(1).getText(); //rate

                    String kas4 = list3.get(0).getText(); //to curr
                    String kas5 = list3.get(1).getText(); //rate

                    //step3 create the statement object
                    try {
                        Statement stmt = con.createStatement();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    String veiksmas = "insert into kursai (CREATED_DATE,REQUEST_DATE,RATE_DATE,FROM_CURR,FROM_AMOUNT,TO_CURR,TO_AMOUNT) " +
                            "values (?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement paruosta = null;
                    try {
                        paruosta = con.prepareStatement(veiksmas);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    Date dabar = new Date(System.currentTimeMillis());

                    try {
                        paruosta.setDate(1, dabar); //CREATED_DATE kada suskurtas irasas
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    try {
                        paruosta.setDate(2, Date.valueOf(kokiaData)); //REQUEST_DATE kokiai datai uzklaustas kursas
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    try {
                        paruosta.setDate(3, Date.valueOf(kas)); //RATE_DATE  kokiai datai pateiktas kursas
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    try {
                        paruosta.setString(4, kas2);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    try {
                        paruosta.setDouble(5, Double.parseDouble(kas3));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    try {
                        paruosta.setString(6, kas4);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    try {
                        paruosta.setDouble(7, Double.parseDouble(kas5));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    try {
                        paruosta.executeUpdate();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                else {
                    //step3 create the statement object
                    try {
                        Statement stmt = con.createStatement();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    String veiksmas = "insert into problems_log (CREATED_DATE,REQUEST_DATE,TO_CURR,PROBLEM) " +
                            "values (?, ?, ?, ?)";

                    PreparedStatement paruosta = null;
                    try {
                        paruosta = con.prepareStatement(veiksmas);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    Date dabar = new Date(System.currentTimeMillis());

                    try {
                        paruosta.setDate(1, dabar); //CREATED_DATE kada suskurtas irasas
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    try {
                        paruosta.setDate(2, Date.valueOf(kokiaData)); //REQUEST_DATE kokiai datai uzklaustas kursas
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    try {
                        paruosta.setString(3, kodas);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    try {
                        paruosta.setString(4, typeContent.getName());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    try {
                        paruosta.executeUpdate();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                }
            } else {

                //step3 create the statement object
                try {
                    Statement stmt = con.createStatement();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                String veiksmas = "insert into problems_log (CREATED_DATE,REQUEST_DATE,TO_CURR,PROBLEM) " +
                        "values (?, ?, ?, ?)";

                PreparedStatement paruosta = null;
                try {
                    paruosta = con.prepareStatement(veiksmas);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                Date dabar = new Date(System.currentTimeMillis());

                try {
                    paruosta.setDate(1, dabar); //CREATED_DATE kada suskurtas irasas
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                try {
                    paruosta.setDate(2, Date.valueOf(kokiaData)); //REQUEST_DATE kokiai datai uzklaustas kursas
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    paruosta.setString(3, kodas);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    paruosta.setString(4, "HttpURLConnection response code: " + responseCode+ " "+ conection.getHeaderField("Location"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                try {
                    paruosta.executeUpdate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }

        @FXML
        public void rodytiKursus2() throws SQLException {
            Parent window1;

            try {
                window1 = FXMLLoader.load(getClass().getResource("/ValiutuKursai.fxml"));
                final Stage window1Stage = new Stage();
                window1Stage.setTitle("Kursai");
                window1Stage.initModality(Modality.APPLICATION_MODAL);
                window1Stage.initOwner(ValiutuKursai2.homeStage);
                Scene window1Scene = new Scene(window1, 1258, 666);
                window1Stage.setScene(window1Scene);
                window1Stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @FXML
        public void rodytiLoga2() throws SQLException {
            Parent window1;

            try {
                window1 = FXMLLoader.load(getClass().getResource("/LogasView.fxml"));
                final Stage window1Stage = new Stage();
                window1Stage.setTitle("Logas");
                window1Stage.initModality(Modality.APPLICATION_MODAL);
                window1Stage.initOwner(ValiutuKursai2.homeStage);
                Scene window1Scene = new Scene(window1, 1258, 666);
                window1Stage.setScene(window1Scene);
                window1Stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @FXML
        public void rodytiPar2() throws SQLException {
            Parent window1;

            try {
                window1 = FXMLLoader.load(getClass().getResource("/ParinktosValiutos.fxml"));
                final Stage window1Stage = new Stage();
                window1Stage.setTitle("Parinktos valiutos");
                window1Stage.initModality(Modality.APPLICATION_MODAL);
                window1Stage.initOwner(ValiutuKursai2.homeStage);
                Scene window1Scene = new Scene(window1, 1258, 666);
                window1Stage.setScene(window1Scene);
                window1Stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @FXML
        public void rodytiHelp() throws SQLException {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(ValiutuKursai2.homeStage);
            VBox dialogVbox = new VBox(20);
            dialogVbox.setAlignment(Pos.CENTER);
            dialogVbox.getChildren().add(new Text("HP Copyright 2020"));
            Scene dialogScene = new Scene(dialogVbox, 600, 400);
            dialog.setScene(dialogScene);
            dialog.show();
        }


    @FXML
    public void initClock() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            dateTime.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
  }


