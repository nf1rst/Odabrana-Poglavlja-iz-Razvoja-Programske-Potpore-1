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
        //https://stackoverflow.com/questions/16708931/javafx-working-with-threads-and-gui
        //https://medium.com/@mglover/concurrency-in-javafx-32a5f6133d
        //https://youtu.be/pdRX6CLP0tM?si=bImZUEx_w1N84C_b
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

        //https://docs.oracle.com/javase/8/javafx/api/javafx/concurrent/Task.html
        Thread thread = new Thread(initTask);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void stop() throws Exception {
        try {
            DataSource.closeConnection();
            //da rucno brisanje u SQL skriptii
        } catch (Exception _) {
            //
        }
        super.stop();
    }
}
