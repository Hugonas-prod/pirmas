import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import model.Kursas;
import model.Sarasas;

import java.net.*;
import java.sql.*;
import java.text.Format;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import java.text.SimpleDateFormat;

public class ValiutuKursaiController implements Initializable {

    private static Connection con;

    @FXML
    private TableView<Kursas> valKursai;

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
   // private TableColumn<Kursas, java.util.Date> CREATED_DATE;
    private TableColumn<Kursas, Date> CREATED_DATE;
    @FXML
    private TextField filterField2;

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

    @FXML
    public void rodytiKursa() throws SQLException {

            con = DriverManager.getConnection(Database.CONNECTION_STRING, Database.USER_STRING, Database.PWD_STRING);
            String query = "SELECT k.ID,k.REQUEST_DATE,k.RATE_DATE,k.FROM_CURR,k.FROM_AMOUNT,k.TO_CURR,k.TO_AMOUNT,k.CREATED_DATE,"
            + "s.PAVADINIMAS FROM kursai k left join sarasas s on k.TO_CURR =s.kodas";
            PreparedStatement statement = null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
            CREATED_DATE.setCellFactory(new LogasController.ColumnFormatter<Kursas,java.util.Date>(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));

        FilteredList<Kursas> filteredData = new FilteredList<>(duomenys, p ->true);

        filterField2.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(kursas -> {
                // If filter text is empty, display all.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (kursas.getTO_CURR().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (kursas.getPAVADINIMAS().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Kursas> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(valKursai.comparatorProperty());
        valKursai.setItems(sortedData);

//            valKursai.setItems(duomenys);
                con.close();
    }

}


