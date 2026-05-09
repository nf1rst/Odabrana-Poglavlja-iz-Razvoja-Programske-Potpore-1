package hr.algebra.controller;
import hr.algebra.models.User;
import hr.algebra.xml.ActionLogger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class BaseController {

    protected User currentUser;

    public void setUser(User user){ // ovdje public jer ce se zvat iz klasa koje ju ne nasljeduju
        currentUser=user;
        onUserSet();
    }

    protected void onUserSet() { //prazna metoda koju klasa koja ovu nsaljeduje moze overrideat ako joj je potrebno da se nesto dogodi nakon postavljanja usera
    //ko onaj default metod u interafceima
    }

    //ovdje protected jer to omogucuje da klase koje nasljede ovu imaju atribute
    protected void openNewWindow(String fxmlPath, String title, User user){//zasto je ovdje object, jel zato da bi se moglo zavorit ili nesto drugo
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());

            Object controller = loader.getController();

            if(controller instanceof BaseController bc)bc.setUser(user);
            if(controller instanceof MainController mc)mc.setUser(user);
            if(controller instanceof AdminController ac)ac.setUser(user);

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.setMinHeight(700);
            stage.setMinWidth(1000);
            stage.show();

            closeThisWindow();
        }
        catch (IOException e){
            showAlert(Alert.AlertType.ERROR, "Error", "Something went wrong");
        }
    }

    private void closeThisWindow() {
        Node node = getMainNode();
        if(node!=null && node.getScene()!=null){
            ((Stage)node.getScene().getWindow()).close();
        }
    }

    protected abstract Node getMainNode(); // svaka podklasa mora dat bilo koji node iz forme da se moze dohvatit window i zatvorit prozor


    protected void showAlert(Alert.AlertType alertType, String title, String info) {
        Alert a = new Alert(alertType);
        a.setTitle(title);
        a.setContentText(info);
        a.showAndWait();
    }

    protected String checkUsernameForLogging() {// ako korisnik nije ulogiran da ne pukne
        return currentUser.getUsername()!=null? currentUser.getUsername():"N/A";
    }

    protected void log(String action, String entity, String details){
        ActionLogger.log(checkUsernameForLogging(),action, entity,details);
    }

    protected boolean requireLogin(){
        if(currentUser==null){
            showAlert(Alert.AlertType.ERROR, "Error", "You must be logged in to edit");
            return false;
        }
        return true;
    }

    @FXML
    public void onMainMenu(){
        if(currentUser==null) openNewWindow("/hr/algebra/views/main-view-anonymus.fxml", "Main anonymus", currentUser);
        else openNewWindow("/hr/algebra/views/main-view-loggedIn.fxml", "Main", currentUser);
    }

    @FXML
    public void onHeadphones(){
        openNewWindow("/hr/algebra/views/headphones-view.fxml","Headphones" ,currentUser);
    }

    @FXML
    public void onBrands(){
        openNewWindow("/hr/algebra/views/brand-view.fxml", "Brands", currentUser);
    }

    @FXML
    public void onTypes(){
        openNewWindow("/hr/algebra/views/types-view.fxml", "Types", currentUser);
    }

    @FXML
    public void onUses(){
        openNewWindow("/hr/algebra/views/uses-view.fxml", "Uses", currentUser);
    }

    @FXML
    public void onTechnologies(){
        openNewWindow("/hr/algebra/views/technology-view.fxml", "Technologies", currentUser);
    }

    @FXML
    public void onCompareHeadphones(){
        openNewWindow("/hr/algebra/views/compare-view.fxml", "Compare headphones", currentUser);
    }

    @FXML
    public void onAccountInfo(){
        if(currentUser==null){
            showAlert(Alert.AlertType.ERROR, "Error", "To view account details, one must be logged in");
            return;
        }
        openNewWindow("/hr/algebra/views/account-details-view.fxml", "Account info", currentUser);
    }

    @FXML
    public void onLogOut(){
        currentUser=null;
        openNewWindow("/hr/algebra/views/login-view.fxml", "Login",currentUser);
    }

    protected boolean showConfirm(String title, String message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        return alert.showAndWait().filter(b->b== ButtonType.OK).isPresent(); // kako ovaj linija radi
    }
}
