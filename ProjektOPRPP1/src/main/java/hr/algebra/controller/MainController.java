package hr.algebra.controller;
import hr.algebra.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class MainController {

    @FXML private Label welcomeLabel;

    private User currentUser;

    public void setUser(User user){
        this.currentUser=user;

        if(currentUser!=null)welcomeLabel.setText("Welcome " + currentUser.getUsername());
        else welcomeLabel.setText("Welcome anonymous");

    }

    @FXML public void onHeadphones(){
        loadView("/hr/algebra/views/headphones-view.fxml", "Headphones Page", currentUser);
    }
    @FXML public void onBrands(){
        loadView("/hr/algebra/views/brand-view.fxml", "Brands Page", currentUser);
    }
    @FXML public void onTechnologies(){
        loadView("/hr/algebra/views/technology-view.fxml", "Technology page", currentUser);
    }
    @FXML public void onCompareHeadphones(){loadView("/hr/algebra/views/compare-view.fxml", "Compare Headphones page", currentUser);}
    @FXML public void onTypes(){loadView("/hr/algebra/views/types-view.fxml", "Type page", currentUser);}
    @FXML public void onUses(){
        loadView("/hr/algebra/views/uses-view.fxml", "Uses page", currentUser);
    }
    @FXML public void onLogin() {loadView("/hr/algebra/views/login-view.fxml", "Login Page", currentUser);}
    @FXML public void onAccountDetails(){
        if(currentUser==null){
            showAlert(Alert.AlertType.ERROR, "Error", "To view account details, one must be logged in");
            return;
        }
        loadView("/hr/algebra/views/account-details-view.fxml", "Account info", currentUser);
    }


    @FXML public void onLogOut(){
        currentUser=null;
        loadView("/hr/algebra/views/login-view.fxml", "Login Page", currentUser);
    }

    private void loadView(String fxmlPath, String title, User user){
        try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Scene scene = new Scene(loader.load());

        //proslijedi korisnika svakom kontrolleru koji ga treba
        Object controller = loader.getController();


            if(controller instanceof BaseController bc)bc.setUser(user);

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.setMinHeight(700);
            stage.setMinWidth(600);
            stage.show();
            closeThisWindow();
        }
        catch(IOException _){
            showAlert(Alert.AlertType.ERROR, "Error", "Something went wrong");
            //System.out.println(e.getMessage()); //OVO TREBA IZBRISAT
        }
    }

    private void closeThisWindow() {
        ((Stage)(welcomeLabel.getScene().getWindow())).close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String info) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(info);
        alert.showAndWait();
    }
}
