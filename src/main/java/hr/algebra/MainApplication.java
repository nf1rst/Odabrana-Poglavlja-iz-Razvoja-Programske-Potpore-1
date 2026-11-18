package hr.algebra;

import hr.algebra.database.DataSource;
import hr.algebra.services.DBInitiatorService;
import hr.algebra.util.LoggingUtil;
import hr.algebra.util.SceneLoadingUtil;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.stage.Stage;


public class MainApplication extends Application {
    public static void main(String[] args)  {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage)  {
        SceneLoadingUtil.loadNewCustomWindow("/hr/algebra/views/login-view.fxml","Headphones - login",null, 400,500);
        initBackground();
    }

    private void initBackground()  {
        Task<Void>initTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                DBInitiatorService dbInitiatorService = DBInitiatorService.INSTANCE;

                dbInitiatorService.insertAdmin();

                dbInitiatorService.insetAPIData();

                return null;
            }
        };

        initTask.setOnFailed(e->LoggingUtil.logError("Failed inserting starting data into database", e));

        Thread thread = new Thread(initTask);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void stop() throws Exception {
        try {
            DataSource.closeConnection();
        } catch (Exception _) {
            //
        }
        super.stop();
    }
}
