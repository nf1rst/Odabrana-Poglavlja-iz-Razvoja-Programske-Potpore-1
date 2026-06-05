package hr.algebra.util;
import com.sun.javafx.util.Logging;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

public class MediaUtil {
    private MediaUtil(){}

     public static void showImage(ImageView imageView, String imagePath) {
        try{
            File imageFile = new File(imagePath);
            if(imageFile.exists()){
                Image placeholder = new Image(imageFile.toURI().toString());
                imageView.setImage(placeholder);
            }
        }
        catch (Exception _){
            AlertUtil.showWarningAlert("No image could be loaded");
        }
    }

    public static void showPlaceholder(ImageView imageView) {
        try{
            InputStream imageStream = MediaUtil.class.getResourceAsStream("/hr/algebra/assets/images/placeholder.png");
            if(imageStream!=null){
                Image placeholder = new Image(imageStream);
                imageView.setImage(placeholder);
                imageStream.close();
            }
        }
        catch (Exception _){
            AlertUtil.showWarningAlert("No placeholder image could be loaded");
        }
    }

    //https://stackoverflow.com/questions/53520358/delete-image-from-the-folder
    //https://stackoverflow.com/questions/20281835/how-to-delete-a-folder-with-files-using-java - jako korisno!!!!!!!!!!!!!!!!!
    public static void deleteImagesInIntelliJ(){
        String imageDir = "src/main/resources/hr/algebra/assets/images/userInput";
        File file= new  File(imageDir);

        try{
            File[]files =file.listFiles();

            if(files!=null){
                for(File f:files){
                    if(f.isFile()){
                        boolean deleted = f.delete();
                        if(!deleted) LoggingUtil.logError("Image deleteion error: " + f.getName());
                    }
                }
            }

        }
        catch (Exception e){
            LoggingUtil.logError("Image file in IntelliJ delete error", e);
            AlertUtil.showErrorAlert("Cannot delete image from IntelliJ");
        }
    }

    //https://teamtreehouse.com/community/setting-selected-image-file-to-imageview-in-javafx
    // Source - https://stackoverflow.com/q/40120133
// Posted by Philayyy, modified by community. See post 'Timeline' for change history
// Retrieved 2026-06-02, License - CC BY-SA 4.0

    public static Optional<String> chooseFile(){
        String destDir = "src/main/resources/hr/algebra/assets/images/userInput/";

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(null);
        try{
            Path targetPath = Paths.get(destDir + selectedFile.getName());
            Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return Optional.of(targetPath.toString());
        }
        catch (Exception e){
            AlertUtil.showWarningAlert("Cannot process selected image");
            LoggingUtil.logError("Cannot copy selected image: " + selectedFile.toPath().toString(), e);
            return Optional.empty();
        }
    }
}
