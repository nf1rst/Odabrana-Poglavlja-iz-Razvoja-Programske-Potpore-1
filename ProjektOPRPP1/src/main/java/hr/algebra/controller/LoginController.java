package hr.algebra.controller;

import hr.algebra.models.Role;
import hr.algebra.models.User;
import hr.algebra.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Predicate;

public class LoginController { //iako ovaj ne treba sve motode iz Base Controllera, bi li ga svejedno trabao nasljedivat
    @FXML private TextField loginUsernameField;
    @FXML private PasswordField loginPasswordField;
    @FXML private Label loginErrorLabel;

    @FXML private TextField registerUsernameField;
    @FXML private PasswordField registerPasswordField;
    @FXML private PasswordField registerConfirmField;
    @FXML private Label registerErrorLabel;


    private final UserService userService = UserService.getInstance();

    private final Predicate<String> notEmpty = s-> s!=null && !s.trim().isEmpty();
    private final Predicate<String> minLength5 = s->s!=null && s.length()>=5;
    private final Predicate<String> minLength7 = s->s!=null && s.length()>=7;
    private final Predicate<String> hasSuspiciusCharacters= s->s.contains("/");



    @FXML //javaFX poziva automatski nakon ucitavanja FXML
    public void initialize(){
        loginErrorLabel.setVisible(false);
        registerErrorLabel.setVisible(false);

        loginPasswordField.setOnKeyPressed(e->{
            if(e.getCode().toString().equals("ENTER")) onLogin();
        });
    }

    @FXML
    public void onLogin(){
        hideMessage(registerErrorLabel);

        String username = loginUsernameField.getText();
        String password = loginPasswordField.getText();

        if(!notEmpty.test(username) ||!notEmpty.test(password)){
            showError(loginErrorLabel, "Fill out all information please");
            loginPasswordField.clear();
            return;
        }

        Optional<User> result = userService.login(username,password);

        if(result.isEmpty()){
            showError(loginErrorLabel, "Incorrect username or password");
            loginPasswordField.clear();
            return;
        }

        User user = result.get();
        if(user.getRole()== Role.ADMIN){
            openWindow("/hr/algebra/views/admin-view.fxml", "Admin Panel", user);
            closeThisWindow();
        }
        else if(user.getRole()==Role.USER){
            //ovo tu ce trebat bit neki main il nesto, za sad je samo eksperimentiranje
            openWindow("/hr/algebra/views/main-view-loggedIn.fxml", "Main Panel", user);
            closeThisWindow();
        }
        closeThisWindow();
    }

    @FXML
    public void onRegister(){
        hideMessage(registerErrorLabel);

        String username = registerUsernameField.getText();
        String password = registerPasswordField.getText();
        String confirm = registerConfirmField.getText();

        if(!minLength5.test(username)){
            showError(registerErrorLabel, "Username must be at least 5 characters long");
            return;
        }
        if(!minLength7.test(password)){
            showError(registerErrorLabel, "Password must have at least 7 characters");
            return;
        }
        if(!password.equals(confirm)){
            showError(registerErrorLabel, "Password do not match, please try again");
            return;
        }
        if(hasSuspiciusCharacters.test(username)){
            showError(registerErrorLabel, "Username cannot contain characters '/'"); // jer se za logiranje koristi N/A ako nije ulogiran
        }
        boolean success = userService.register(username,password);
        if(!success) {
            showError(registerErrorLabel, "Username already exists");
            return;
        }
        showSuccess(registerErrorLabel, "Registration successful, you can now login");

        registerUsernameField.clear();
        registerConfirmField.clear();
        registerPasswordField.clear();
    }

    @FXML
    public void onWithoutAccount(){
        openWindow("/hr/algebra/views/main-view-anonymus.fxml", "Main screen", null); //null jer znaci da korisnik klinuo da zeli razgledavat pa se nije ni mono ulogirat tad
    }

    private void openWindow(String fxmlPath, String title, User user){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());

            Object controller = loader.getController();

            //!!!!!!!!!!!!!!!!!!!!! ovo je dobra putanja ako ces na kraju imat neki main meni za sve i kad ces dodavat admina


            if(controller instanceof MainController mc) mc.setUser(user);

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.setMinHeight(700);
            stage.setMinWidth(1000);
            stage.show();
            closeThisWindow();
        }
        catch (IOException e){
            showError(loginErrorLabel, "Cannot open the window: " + e.getMessage());
            //System.out.println(e.getMessage() + "!!!!!!!!!!!!!!!!!!!!!!!!!! " +e.getCause());
        }
    }

    private void closeThisWindow() {
        ((Stage) loginUsernameField.getScene().getWindow()).close();
    }

    private void showError(Label label, String message){
        label.setText(message);
        label.setStyle("-fx-test-fill: red;");
        label.setVisible(true);
    }

    private void hideMessage(Label label){
        label.setVisible(false);
        label.setText("");
    }

    private void showSuccess(Label label, String message){

        label.setText(message);
        label.setStyle("-fx-test-fill: green;");
        label.setVisible(true);
    }

}
