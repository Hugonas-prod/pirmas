import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import model.Logas;
import model.Sarasas;
import java.net.*;
import java.sql.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

    public class PilnasSarController implements Initializable {

        private static Connection con;

        @FXML
        private TableView<Sarasas> pilnasSar;
        @FXML
        private TableColumn<Sarasas, String> Kodas;
        @FXML
        private TableColumn<Sarasas, String> PavadinimasVal;
        @FXML
        private TableColumn<Sarasas, java.util.Date> IrasoData;
        @FXML
        private TextField filterField;

        public PilnasSarController() throws ClassNotFoundException, SQLException {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
                 try {
                rodytiSarasa();
               } catch (SQLException throwables) {
                    throwables.printStackTrace();
               }
        }
        @FXML
        public void rodytiSarasa() throws SQLException {

            con = DriverManager.getConnection(Database.CONNECTION_STRING, Database.USER_STRING, Database.PWD_STRING);
            String query = "SELECT KODAS , PAVADINIMAS, CREATED_DATE FROM sarasas";
            PreparedStatement statement = null;
            try {
                statement = con.prepareStatement(query);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            ResultSet resultSet = null;
            resultSet = statement.executeQuery();

            ArrayList<Sarasas> data =  new ArrayList<>();
            while (resultSet.next()) {

                Sarasas vienas = new Sarasas();
                vienas.setKodas(resultSet.getString("KODAS"));
                vienas.setPavadinimasVal(resultSet.getString("PAVADINIMAS"));
                vienas.setIrasoData(resultSet.getDate("CREATED_DATE"));
                data.add(vienas);
            }
            ObservableList duomenys = FXCollections.observableArrayList(data);
            Kodas.setCellValueFactory(new PropertyValueFactory<Sarasas, String>("Kodas"));
            PavadinimasVal.setCellValueFactory(new PropertyValueFactory<Sarasas, String>("PavadinimasVal"));
            IrasoData.setCellValueFactory(new PropertyValueFactory<Sarasas, java.util.Date>("IrasoData"));
            IrasoData.setCellFactory(new LogasController.ColumnFormatter<Sarasas, Date>(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));

            FilteredList<Sarasas> filteredData = new FilteredList<>(duomenys,p ->true);

            filterField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(sarasas -> {
                    // If filter text is empty, display all.
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (sarasas.getKodas().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Filter matches first name.
                    } else if (sarasas.getPavadinimasVal().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });
            SortedList<Sarasas> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(pilnasSar.comparatorProperty());
            pilnasSar.setItems(sortedData);

//                    pilnasSar.setItems(duomenys);
            con.close();

        }

    }


