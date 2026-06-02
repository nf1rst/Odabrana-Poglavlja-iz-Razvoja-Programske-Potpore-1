package hr.algebra.controller;

import hr.algebra.models.Session;
import hr.algebra.services.UserService;
import hr.algebra.util.LoggingUtil;
import hr.algebra.util.MediaUtil;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class UserController extends BaseController {

    @FXML ImageView profileImageView;
    @FXML Label usernameLabel;
    @FXML Label roleLabel;
    @FXML Label statusLabel;
    @FXML Label imageStatusLabel;
    @FXML TextField newUsernameField;
    @FXML TextField confirmUsernameField;

    private final UserService userService=UserService.getInstance();

    @FXML
    public void initialize(){
        statusLabel.setText("");
        imageStatusLabel.setText("");
        onUserSet();
    }

    @Override
    protected void onUserSet() {
        usernameLabel.setText(Session.getCurrentUser().getUsername());
        roleLabel.setText(Session.getCurrentUser().getRole().toString());

        MediaUtil.showImage(profileImageView, "/hr/algebra/assets/images/noPicture.png");
    }

    @Override
    protected Node getMainNode() {
        return roleLabel;
    }

    @FXML
    public void onSaveChanges(){
        boolean changed=false;

        String newUsername = newUsernameField.getText();
        String confirmNewUsername=confirmUsernameField.getText();

        if(!newUsername.isEmpty()){
            if(newUsername.length()<5){
                setStatus("Username must have at least 5 characters");
                return;
            }
            if(!newUsername.equals(confirmNewUsername)){
                setStatus("Usernames do not match");
                return;
            }
            if(newUsername.contains("/")){
                setStatus("Cannot use character '/'");
                return;
            }
            currentUser.setUsername(newUsername);
            changed = true;

            usernameLabel.setText(Session.getCurrentUser().getUsername());
        }

        if(!changed){
            setStatus("You've made no changes");
            return;
        }

        userService.updateUser(currentUser);

        LoggingUtil.logInfo( "UPDATE", "User", "Account updated",currentUser.getUsername());

        setStatus("Changes saved successfully");

        newUsernameField.clear();
        confirmUsernameField.clear();
    }

    @FXML
    public void onCancel(){
        newUsernameField.clear();
        confirmUsernameField.clear();
    }

    private void setStatus(String s) {
        statusLabel.setText(s);
    }

}
