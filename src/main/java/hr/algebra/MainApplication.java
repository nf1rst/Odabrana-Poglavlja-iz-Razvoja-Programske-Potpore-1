package hr.algebra;

import hr.algebra.database.DBSingleton;
import hr.algebra.services.DBInitiatorService;
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
    public void start(Stage primaryStage)  {
        SceneLoadingUtil.loadNewCustomWindow("/hr/algebra/views/login-view.fxml","Headphones - login",null, 400,500);
        initBackground();
    }

    private void initBackground(){
        Task<Void>initTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                DBInitiatorService dbInitiatorService = DBInitiatorService.INSTANCE;

                dbInitiatorService.insertAdmin();

                dbInitiatorService.insetAPIData();

                return null;
            }
        };

        //ne treba success jer svaka odvojeno biljezi uspjeh
        initTask.setOnFailed(e->LoggingUtil.logError("Failed inserting starting data into database", e));

        Thread thread = new Thread(initTask);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void stop() throws Exception {
        try {
            DBSingleton.INSTANCE.closeConnection();
            //da rucno brisanje u SQL skriptii
        } catch (Exception _) {
            //
        }
        super.stop();
    }
}
