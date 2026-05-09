package hr.algebra;
import hr.algebra.database.DBSingleton;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApplication extends Application {
    public static void main(String[] args)  {

        //ISPRAVI SVE THROW NEW RUNTIMEEXCEPTIONS U NESTO KORISNO!!

        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/hr/algebra/views/login-view.fxml"));
        //ovo zasad ne treba bit prvo da mogu provjeravat jel ostale stvari rade

        //FXMLLoader loader =new  FXMLLoader(getClass().getResource("/hr/algebra/views/brand-view.fxml"));
        //Scene scene = new Scene(loader.load());


        //moze i Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        Scene scene = new Scene(loader.load(),420,520); //ove dimenzije su za login screen
        primaryStage.setTitle("Headphones - Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        try {
            DBSingleton.getInstance().closeConnection();
        } catch (Exception ignored) {}
        super.stop();
    }
}
