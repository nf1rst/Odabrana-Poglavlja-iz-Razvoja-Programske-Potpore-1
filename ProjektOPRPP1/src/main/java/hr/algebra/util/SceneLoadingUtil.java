package hr.algebra.util;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SceneLoadingUtil {
    private static final Logger logger = LoggerFactory.getLogger(SceneLoadingUtil.class);

    private SceneLoadingUtil() {}

    public static void loadNewWindow(String fxmlPath, String title, Node node){ // node sluzi za zatvranje trenutnog prozora
        try{
            FXMLLoader loader = new FXMLLoader(SceneLoadingUtil.class.getResource(fxmlPath));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setWidth(1020);
            stage.setHeight(800);
            stage.show();

            closeThisWindow(node);
        }
        catch (IOException e){
            AlertUtil.showErrorAlert("Cannot open new window");
            LoggingUtil.logError("FXML error ", fxmlPath, e);
        }
    }


    public static void loadPopUpWindow(String fxmlPath, String title){
        try{
            FXMLLoader loader = new FXMLLoader(SceneLoadingUtil.class.getResource(fxmlPath));
            Scene scene = new Scene(loader.load());

            Stage stage = new  Stage();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setWidth(400);
            stage.setHeight(320);
            stage.show();
        }
        catch (IOException e){
            AlertUtil.showErrorAlert("Cannot load new window");
            LoggingUtil.logError("FXML load error ", fxmlPath, e);
        }
    }

    public static void loadNewCustomWindow(String fmxlPath, String title, Node node, int width, int height){
        try{
            FXMLLoader loader = new FXMLLoader(SceneLoadingUtil.class.getResource(fmxlPath));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setWidth(width);
            stage.setHeight(height);
            stage.show();

            if(node!=null)closeThisWindow(node); //ako se posalje da je node null nece se zatvorit
        }
        catch (IOException e){
            AlertUtil.showErrorAlert("Cannot load new window");
            LoggingUtil.logError("FXML load error ", fmxlPath, e);
        }
    }

    private static void closeThisWindow(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }


}
