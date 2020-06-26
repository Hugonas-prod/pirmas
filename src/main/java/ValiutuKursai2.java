import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class ValiutuKursai2 extends Application {
    public Stage primaryStage;

  //  public static Stage primaryStage;
  //  public static Stage parentWindow;

    public static Stage homeStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        homeStage = primaryStage;

        ResourceBundle bundle = ResourceBundle.getBundle("UIResources",new Locale("lt"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ValiutuKursai2.fxml"), bundle);

        Parent root = loader.load();

        primaryStage.setScene(new Scene(root));

        primaryStage.getIcons().add(new Image("/icons8-exchange-euro-48.png"));
        primaryStage.setTitle(bundle.getString("titulinis"));

        primaryStage.show();
    }

}

