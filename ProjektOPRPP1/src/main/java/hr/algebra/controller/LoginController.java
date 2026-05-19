package hr.algebra.controller;
import hr.algebra.exceptions.EntityExistantException;
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

        if(!validateLoginUserInput(username, password)){
            return;
        }

        Optional<User> result=Optional.empty(); // da ga dole mogu provjerit jel spremio ista u varijablu

        try{
            result = userService.login(username,password);
        }
        catch (EntityExistantException e){
            showError(loginErrorLabel, e.getMessage()); // ako ga tu uhvati znaci da ne postoji
            return;
        }

        if(result.isEmpty()){
            showError(loginErrorLabel, "Incorrect password"); // ako prode do tu znaci da username postoji al password nije dobar
            loginPasswordField.clear();
            return;
        }

        User currentUser = result.get();
        handleUserRedirection(currentUser);
        closeThisWindow();
    }

    private boolean validateLoginUserInput(String username, String password) {
        if(username.isEmpty() || password.isEmpty()){
            showError(registerErrorLabel, "Username and password cannot be empty");
            return false;
        }
        return true;
    }

    private void handleUserRedirection(User user) {
        if(user.getRole()== Role.ADMIN){
            openWindow("/hr/algebra/views/admin-view.fxml", "Admin Panel", user);
            closeThisWindow();
        }
        else if(user.getRole()==Role.USER){
            //ovo tu ce trebat bit neki main il nesto, za sad je samo eksperimentiranje
            openWindow("/hr/algebra/views/main-view-loggedIn.fxml", "Main Panel", user);
            closeThisWindow();
        }
    }

    @FXML
    public void onRegister(){
        hideMessage(registerErrorLabel);

        String username = registerUsernameField.getText();
        String password = registerPasswordField.getText();
        String confirm = registerConfirmField.getText();

        if(!validateRegisterUserInput(username, password, confirm)){
            return;
        }

        try{
            userService.register(username,(password));
            showSuccess(registerErrorLabel, "Registration successful. You can now log in");
        }
        catch (EntityExistantException e){
            showError(registerErrorLabel, e.getMessage() + "!");
            return;
        }
        
        clearUserInput();
    }

    private void clearUserInput() {
        registerUsernameField.clear();
        registerConfirmField.clear();
        registerPasswordField.clear();
    }

    private boolean validateRegisterUserInput(String username, String password, String confirm) {
        if(!minLength5.test(username)){
            showError(registerErrorLabel, "Username must be at least 5 characters long");
            return false;
        }
        if(!minLength7.test(password)){
            showError(registerErrorLabel, "Password must have at least 7 characters");
            return false;
        }
        if(!password.equals(confirm)){
            showError(registerErrorLabel, "Password do not match, please try again");
            return false;
        }
        if(hasSuspiciusCharacters.test(username)){
            showError(registerErrorLabel, "Username cannot contain characters '/'"); // jer se za logiranje koristi N/A ako nije ulogiran
            return false;
        }
        return true;
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
