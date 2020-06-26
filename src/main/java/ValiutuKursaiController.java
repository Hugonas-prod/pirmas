import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Kursas;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import java.util.*;

public class ValiutuKursaiController implements Initializable {

    private static Connection con;

    @FXML
    private TableView<Kursas> valKursai;

    @FXML
    private MenuItem gautiSar;
    @FXML
    private MenuItem rodytiSar;
    @FXML
    private MenuItem rodytiKur;
    @FXML
    private MenuItem rodytiApie;

    @FXML
    private ObservableList<Kursas> duomenys;

    @FXML
    private TableColumn<Kursas, Integer> ID;
    @FXML
    private TableColumn<Kursas, java.util.Date> REQUEST_DATE;
    @FXML
    private TableColumn<Kursas, java.util.Date> RATE_DATE;
    @FXML
    private TableColumn<Kursas, String> FROM_CURR;
    @FXML
    private TableColumn<Kursas, Integer> FROM_AMOUNT;
    @FXML
    private TableColumn<Kursas, String> TO_CURR;
    @FXML
    private TableColumn<Kursas, String> PAVADINIMAS;
    @FXML
    private TableColumn<Kursas, Double> TO_AMOUNT;
    @FXML
    private TableColumn<Kursas, java.util.Date> CREATED_DATE;

    public ValiutuKursaiController() throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            rodytiKursa();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

//    @FXML
//    public void rodytiSarasa() throws SQLException {
//        Parent window1;
//        try {
//            window1 = FXMLLoader.load(getClass().getResource("/PilnasSarasas.fxml"));
//            Stage window1Stage;
//            Scene window1Scene = new Scene(window1, 1258, 666);
//            window1Stage = ValiutuKursai2.homeStage;
//            window1Stage.setScene(window1Scene);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @FXML
    public void rodytiKursa() throws SQLException {

            con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.2.154:1521:xe", "VARTOTOJAS", "vartotojas");
            String query = "SELECT k.ID,k.REQUEST_DATE,k.RATE_DATE,k.FROM_CURR,k.FROM_AMOUNT,k.TO_CURR,k.TO_AMOUNT,k.CREATED_DATE,"
            + "s.PAVADINIMAS FROM kursai k left join sarasas s on k.TO_CURR =s.kodas";
            PreparedStatement statement = null;
            try {
                statement = con.prepareStatement(query);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            ResultSet resultSet = null;
            resultSet = statement.executeQuery();

            ArrayList<Kursas> data =  new ArrayList<>();
            while (resultSet.next()) {

                Kursas vienas = new Kursas();
                vienas.setID(resultSet.getInt("ID"));
                vienas.setREQUEST_DATE(resultSet.getDate("REQUEST_DATE"));
                vienas.setRATE_DATE(resultSet.getDate("RATE_DATE"));
                vienas.setFROM_CURR(resultSet.getString("FROM_CURR"));
                vienas.setFROM_AMOUNT(resultSet.getInt("FROM_AMOUNT"));
                vienas.setTO_CURR(resultSet.getString("TO_CURR"));
                vienas.setTO_AMOUNT(resultSet.getDouble("TO_AMOUNT"));
                vienas.setCREATED_DATE(resultSet.getDate("CREATED_DATE"));
                vienas.setPAVADINIMAS(resultSet.getString("PAVADINIMAS"));
                data.add(vienas);
            }
            ObservableList duomenys = FXCollections.observableArrayList(data);
            ID.setCellValueFactory(new PropertyValueFactory<Kursas, Integer>("ID"));
            REQUEST_DATE.setCellValueFactory(new PropertyValueFactory<Kursas, java.util.Date>("REQUEST_DATE"));
            RATE_DATE.setCellValueFactory(new PropertyValueFactory<Kursas, java.util.Date>("RATE_DATE"));
            FROM_CURR.setCellValueFactory(new PropertyValueFactory<Kursas, String>("FROM_CURR"));
            FROM_AMOUNT.setCellValueFactory(new PropertyValueFactory<Kursas, Integer>("FROM_AMOUNT"));
            TO_CURR.setCellValueFactory(new PropertyValueFactory<Kursas, String>("TO_CURR"));
            PAVADINIMAS.setCellValueFactory(new PropertyValueFactory<Kursas, String>("PAVADINIMAS"));
            TO_AMOUNT.setCellValueFactory(new PropertyValueFactory<Kursas, Double>("TO_AMOUNT"));
            CREATED_DATE.setCellValueFactory(new PropertyValueFactory<Kursas, java.util.Date>("CREATED_DATE"));

            valKursai.setItems(duomenys);
                con.close();
    }

//    @FXML
//    public void gautiSarasaXML(ActionEvent actionEvent) {
//        try {
//            String uri = "https://www.lb.lt/webservices/ExchangeRates/ExchangeRates.asmx/getListOfCurrencies";
//
//            URLConnection urlConnection = new URL(uri).openConnection();
//            urlConnection.setUseCaches(false);
//            urlConnection.setDoOutput(true); // Triggers POST.
//            urlConnection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
//
//            OutputStreamWriter writer = null;
//            try {
//                writer = new OutputStreamWriter(urlConnection.getOutputStream());
//                writer.write("POST"); // Write POST query string (if any needed).
//            } finally {
//                if (writer != null) try {
//                    writer.close();
//                } catch (IOException logOrIgnore) {
//                }
//            }
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
//
//            String line = "";
//            String xmlResponse = "";
//            while ((line = reader.readLine()) != null) {
//                xmlResponse += line;
//            }
//
//            SAXBuilder builder = new SAXBuilder();
//            Document document = builder.build(new ByteArrayInputStream(xmlResponse.getBytes("UTF-8")));
//            Element rootNode;
//            rootNode = document.getRootElement();
//            List list = rootNode.getChildren("item");
//
//            //step2 create  the connection object
//            con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.2.154:1521:xe", "VARTOTOJAS", "vartotojas");
//            //step3 create the statement object
//            Statement stmt = con.createStatement();
//
//            //step4 execute query
//            stmt.executeQuery("delete from sarasas");
//
//            String veiksmas = "insert into sarasas (kodas,pavadinimas) values (?, ?)";
//            PreparedStatement paruosta = con.prepareStatement(veiksmas);
//
//            int list_size = list.size();
//            for (int i = 0; i < list.size(); i++) {
//
//                Element node = (Element) list.get(i);
//                List<Element> eles = node.getChildren("description");
//
//                paruosta.setString(1, node.getChildText("currency"));
//                paruosta.setString(2, eles.get(1).getValue());
//                paruosta.executeUpdate();
//            }
//            con.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    @FXML
//    public void rodytiKursus() throws SQLException {
//        Parent window1;
//
//        try {
//            window1 = FXMLLoader.load(getClass().getResource("/ValiutuKursai.fxml"));
//            Stage window1Stage;
//            Scene window1Scene = new Scene(window1, 1258, 666);
//            window1Stage = ValiutuKursai2.homeStage;
//            window1Stage.setScene(window1Scene);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

//    @FXML
//    public void rodytiLoga() throws SQLException {
//        Parent window1;
//
//        try {
//            window1 = FXMLLoader.load(getClass().getResource("/LogasView.fxml"));
//            Stage window1Stage;
//            Scene window1Scene = new Scene(window1, 1258, 666);
//            window1Stage = ValiutuKursai2.homeStage;
//            window1Stage.setScene(window1Scene);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

//    @FXML
//    public void rodytiHelp() throws SQLException {
//        final Stage dialog = new Stage();
//        dialog.initModality(Modality.APPLICATION_MODAL);
//        dialog.initOwner(ValiutuKursai2.homeStage);
//        VBox dialogVbox = new VBox(20);
//        dialogVbox.getChildren().add(new Text("Helpas\n" + "HP Copyright 2020"));
//        Scene dialogScene = new Scene(dialogVbox, 300, 200);
//        dialog.setScene(dialogScene);
//        dialog.show();
//
//    }
}

