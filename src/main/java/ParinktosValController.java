import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Sarasas;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ParinktosValController implements Initializable {

    private static Connection con;

    @FXML
    private TableView<Sarasas> parinktosVal;
    @FXML
    private TableColumn<Sarasas, String> Kodas;
    @FXML
    private TableColumn<Sarasas, String> PavadinimasVal;
    @FXML
    private TableColumn<Sarasas, java.util.Date> IrasoData;
    @FXML
    private ListView visosValiutos;

    public ParinktosValController() throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            uzpildytiSarasa();
            rodytiPasirinktas();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    @FXML
    public void rodytiPasirinktas() throws SQLException {

        con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.2.154:1521:xe", "VARTOTOJAS", "vartotojas");
        String query = "select pas.KODAS,sar.PAVADINIMAS,pas.CREATED_DATE from pasirinktos pas left join sarasas sar on pas.kodas= sar.kodas";
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
        parinktosVal.setItems(duomenys);
        con.close();
    }

    @FXML
    public void uzpildytiSarasa() throws SQLException {
            ArrayList <String> currList = new ArrayList<>();
            ListProperty<String> listProperty = new SimpleListProperty<>();
            con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.2.154:1521:xe", "VARTOTOJAS", "vartotojas");
            String query = "SELECT kodas,pavadinimas FROM sarasas order by pavadinimas";
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
                String pavadinimas = resultSet.getString("pavadinimas");
                currList.add(kodas + " " + pavadinimas);
            }
            visosValiutos.itemsProperty().bind(listProperty);
            visosValiutos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            listProperty.set(FXCollections.observableArrayList(currList));
            con.close();
        }

     @FXML
     public void saugotiParinktas() {
            try {
                con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.2.154:1521:xe", "VARTOTOJAS", "vartotojas");
                Statement stmt = con.createStatement();
                stmt.executeQuery("delete from pasirinktos");

                String veiksmas = "insert into pasirinktos (kodas) values (?)";
                PreparedStatement paruosta = con.prepareStatement(veiksmas);
                ObservableList<String> topics = visosValiutos.getSelectionModel().getSelectedItems();

                for(String vienas : topics){
                    paruosta.setString(1, vienas.substring(0,3));
                    paruosta.executeUpdate();
                }
                con.close();

                rodytiPasirinktas();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
