package hr.algebra.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertUtil {
    private AlertUtil() {}

    public static void showErrorAlert(String text){
        Alert alert =  new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(text);
        alert.showAndWait();
    }

    public static void showInformationAlert (String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(text);
        alert.showAndWait();
    }

    public static void showWarningAlert(String text){
        Alert alert =  new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setContentText(text);
        alert.showAndWait();
    }

    public static boolean showConfirmationAlert(String text){
        Alert alert =  new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText(text);
        alert.showAndWait();
        return alert.getResult()==ButtonType.OK;
    }

}
