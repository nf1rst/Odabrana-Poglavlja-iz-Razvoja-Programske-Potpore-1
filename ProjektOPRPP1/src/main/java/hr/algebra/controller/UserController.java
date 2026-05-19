package hr.algebra.controller;
import hr.algebra.services.UserService;
import hr.algebra.xml.ActionLogger;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class UserController extends BaseController {

    @FXML ImageView profileImageView;
    @FXML Label usernameLabel;
    @FXML Label roleLabel;
    @FXML Label statusLabel;
    @FXML Label imageStatusLabel;
    @FXML TextField newPasswordField;
    @FXML TextField confirmPasswordField;

    private String selectedImagePath;

    private final UserService userService=UserService.getInstance();

    @FXML
    public void initialize(){
        statusLabel.setText("");
        imageStatusLabel.setText("");
    }

    @Override
    protected void onUserSet() {
        if(currentUser==null)return; // je li ovo nepotrebno s obizorm da ne ulogirani korisnici ne mogu gledat ovaj view

        usernameLabel.setText(currentUser.getUsername());
        roleLabel.setText(currentUser.getRole().toString());

        loadProfileImage(currentUser.getImagePath());
    }

    private void loadProfileImage(String imagePath) {
        if(imagePath==null ||imagePath.isEmpty()){
            showPlaceHolderImage();
            return;
        }
        try{
            File imageFile = new File(imagePath);
            if(!imageFile.exists()){
                showPlaceHolderImage();
                return;
            }
            Image image = new Image(new FileInputStream(imageFile));
            profileImageView.setImage(image);
            imageStatusLabel.setText(imageFile.getName());
        }
        catch (Exception _){
            showPlaceHolderImage();
        }
    }

    private void showPlaceHolderImage() {
        try{
            var placeholder = getClass().getResourceAsStream("/hr/algebra/assets/placeholder.png");
            if(placeholder!=null){
                profileImageView.setImage(new Image(placeholder));
            }
        }
        catch(Exception _){
            imageStatusLabel.setText("No profile picture");
        }
    }

    @Override
    protected Node getMainNode() {
        return roleLabel;
    }

    @FXML
    public void onSetProfilePicture(){
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose a profile picture");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));

        File file = fc.showOpenDialog(
                usernameLabel.getScene().getWindow());
        if(file==null)return;
        try{
            Path assetsDir=Paths.get("src/main/resources/algebra/assets/");
            Files.createDirectories(assetsDir);

            String extension = getExtension(file.getName());
            String newFileName = "profile" + currentUser.getUsername()  + extension;

            Path dest = assetsDir.resolve(newFileName);
            Files.copy(file.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
            selectedImagePath=dest.toString();

            Image image = new Image(new FileInputStream(dest.toFile()));
            profileImageView.setImage(image);
            imageStatusLabel.setText(newFileName);
            setStatus("Picture selected. Click 'Save changes' to save");
        }
        catch(Exception _){
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot fetch the image");
        }
    }

    @FXML
    public void onSaveChanges(){
        boolean changed=false;

        String newPasword = newPasswordField.getText();
        String conirmPassword=confirmPasswordField.getText();

        if(!newPasword.isEmpty()){
            if(newPasword.length()<7){
                setStatus("Password must have at least 7 characters");
                return;
            }
            if(!newPasword.equals(conirmPassword)){
                setStatus("Password do not match");
                return;
            }
            if(newPasword.contains("/")){
                setStatus("Cannot use character '/'");
                return;
            }
            currentUser.setPassword(newPasword);
            changed = true;
        }

        //slika
        if(selectedImagePath!=null){
            currentUser.setImagePath(selectedImagePath);
            changed=true;
        }

        if(!changed){
            setStatus("You've made no changes");
            return;
        }

        userService.updateUser(currentUser);

        ActionLogger.log(currentUser.getUsername(), "UPDATE", "User", "Account updated");

        setStatus("Changes saved successfully");

        newPasswordField.clear();
        confirmPasswordField.clear();
    }

    @FXML
    public void onCancel(){
        Image oldProfilePicture=null;
        try{
            oldProfilePicture = new Image(new FileInputStream(new File(currentUser.getImagePath())));
        }
        catch (Exception _){
            // samo neka ostavi na null da prikaze staru sliku
        }
        profileImageView.setImage(oldProfilePicture); // da makne sliku ako klikne cancel
        newPasswordField.clear();
        confirmPasswordField.clear();
    }

    private void setStatus(String s) {
        statusLabel.setText(s);
    }

    private String getExtension(String fileName) {
        int dot = fileName.lastIndexOf('.');
        return dot>=0?fileName.substring(dot):".png";
    }

}
