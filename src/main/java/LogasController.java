import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import model.Kursas;
import model.Logas;
import java.net.*;
import java.sql.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;


public class LogasController  implements Initializable {

    private static Connection con;

    @FXML
    private TableView<Logas> logasErrors;

    @FXML
    private MenuItem gautiSar;
    @FXML
    private MenuItem rodytiSar;
    @FXML
    private MenuItem rodytiErr;
    @FXML
    private MenuItem rodytiApie;

    @FXML
    private TableColumn<Logas, Integer> ID;
    @FXML
    private TableColumn<Logas, Date> REQUEST_DATE;
    @FXML
    private TableColumn<Logas, String> TO_CURR;
    @FXML
    private TableColumn<Logas, java.util.Date> CREATED_DATE;
    @FXML
    private TableColumn<Logas, String> PROBLEM;

    public LogasController () throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            rodytiLoga();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    public void rodytiLoga() throws SQLException {

        con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.2.154:1521:xe", "VARTOTOJAS", "vartotojas");
        String query = "SELECT ID, REQUEST_DATE, TO_CURR, CREATED_DATE, PROBLEM FROM problems_log";
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ResultSet resultSet = null;
        resultSet = statement.executeQuery();

        ArrayList<Logas> data =  new ArrayList<>();
        while (resultSet.next()) {

            Logas vienas = new Logas();
            vienas.setID(resultSet.getInt("ID"));
            vienas.setREQUEST_DATE(resultSet.getDate("REQUEST_DATE"));
            vienas.setTO_CURR(resultSet.getString("TO_CURR"));
            vienas.setCREATED_DATE(resultSet.getDate("CREATED_DATE"));
            vienas.setPROBLEM(resultSet.getString("PROBLEM"));

            data.add(vienas);
        }
        ObservableList duomenys = FXCollections.observableArrayList(data);
        ID.setCellValueFactory(new PropertyValueFactory<Logas, Integer>("ID"));
        REQUEST_DATE.setCellValueFactory(new PropertyValueFactory<Logas, java.util.Date>("REQUEST_DATE"));
        TO_CURR.setCellValueFactory(new PropertyValueFactory<Logas, String>("TO_CURR"));
        CREATED_DATE.setCellValueFactory(new PropertyValueFactory<Logas, java.util.Date>("CREATED_DATE"));
        CREATED_DATE.setCellFactory(new LogasController.ColumnFormatter<Logas, Date>(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
        PROBLEM.setCellValueFactory(new PropertyValueFactory<Logas, String>("PROBLEM"));
        logasErrors.setItems(duomenys);
        con.close();

    }

    public static class ColumnFormatter<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {
        private Format format;

        public ColumnFormatter(Format format) {
            super();
            this.format = format;
        }
        @Override
        public TableCell<S, T> call(TableColumn<S, T> arg0) {
            return new TableCell<S, T>() {
                @Override
                protected void updateItem(T item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(new Label(format.format(item)));
                    }
                }
            };
        }
    }
}

