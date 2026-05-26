package hr.algebra;
import hr.algebra.database.DBSingleton;
import hr.algebra.exceptions.EntityExistantException;
import hr.algebra.services.UserService;
import hr.algebra.util.LoggingUtil;
import hr.algebra.util.SceneLoadingUtil;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.stage.Stage;


public class MainApplication extends Application {
    public static void main(String[] args)  {

        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneLoadingUtil.loadNewCustomWindow("/hr/algebra/views/login-view.fxml","Headphones - login",null, 400,500);
        initBackground();
    }

    private void initBackground() {
        Task<Void> init = new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                insertAdmin();
                return null;
            }
        };

        init.setOnSucceeded(e->LoggingUtil.logInfo("Admin added"));

        init.setOnFailed(e->LoggingUtil.logError("Admin already exists"));
        //failed se okine ako dode unhandled greska - EntityNotExistantException iz insertAdmin

        Thread bgThread = new Thread(init);
        bgThread.setDaemon(true);
        bgThread.start();
    }

    private void insertAdmin() throws EntityExistantException {
        final UserService userService = UserService.getInstance();

        userService.registerAdmin("admin", "asdfasdf");

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
