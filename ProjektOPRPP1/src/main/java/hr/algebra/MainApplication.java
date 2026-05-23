package hr.algebra;
import hr.algebra.database.DBSingleton;
import hr.algebra.services.UserService;
import hr.algebra.util.SceneLoadingUtil;
import javafx.application.Application;
import javafx.stage.Stage;


public class MainApplication extends Application {
    public static void main(String[] args)  {

        //ISPRAVI SVE THROW NEW RUNTIMEEXCEPTIONS U NESTO KORISNO!!

        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneLoadingUtil.loadNewCustomWindow("/hr/algebra/views/login-view.fxml","Headphones - login",null, 400,500);
        insertAdmin();
    }

    private void insertAdmin() {
        final UserService userService = UserService.getInstance();

        userService.registerAdmin("admin", "asdfasdf");
        //ovaj ne treba u try catch jer se greska hvata u userService
    }

    @Override
    public void stop() throws Exception {
        try {
            DBSingleton.INSTANCE.closeConnection();
        } catch (Exception _) {
            //
        }
        super.stop();
    }
}
