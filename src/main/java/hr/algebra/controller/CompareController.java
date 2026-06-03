package hr.algebra.controller;
import hr.algebra.models.Headphones;
import hr.algebra.models.Technology;
import hr.algebra.models.Use;
import hr.algebra.services.HeadphonesService;
import hr.algebra.util.AlertUtil;
import hr.algebra.util.MediaUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.stream.Collectors;

public class CompareController extends BaseController {

    @FXML private ListView<Headphones>headphonesListView;

    @FXML private NavigationController  navigationController;

    @FXML private Label viewHeadphoneName1;
    @FXML private Label viewHeadphoneName2;

    @FXML private ImageView headphones1ImageView;
    @FXML private ImageView headphones2ImageView;

    @FXML private Label currentUserLabel;

    @FXML private Label nameLabel1;
    @FXML private Label nameLabel2;
    @FXML private Label yearLabel1;
    @FXML private Label yearLabel2;
    @FXML private Label impedanceLabel1;
    @FXML private Label impedanceLabel2;
    @FXML private Label minFreqLabel1;
    @FXML private Label minFreqLabel2;
    @FXML private Label maxFreqLabel1;
    @FXML private Label maxFreqLabel2;
    @FXML private Label batteryLabel1;
    @FXML private Label batteryLabel2;
    @FXML private Label priceLabel1;
    @FXML private Label priceLabel2;
    @FXML private Label descLabel1;
    @FXML private Label descLabel2;
    @FXML private Label brandLabel1;
    @FXML private Label brandLabel2;
    @FXML private Label techLabel1;
    @FXML private Label techLabel2;
    @FXML private Label usesLabel1;
    @FXML private Label usesLabel2;
    @FXML private Label typesLabel1;
    @FXML private Label typesLabel2;

    @FXML private VBox dropZone1;
    @FXML private VBox dropZone2;

    private final HeadphonesService headphonesService=HeadphonesService.getInstance();
    private Headphones selectedHeadphones;

    private Headphones headphonesForComparing1;
    private Headphones headphonesForComparing2;
    @FXML
    public void initialize(){
        initListView();
        setUpDragSource();
        setupDropZones(); //kad dode dovoljno blizu da krene zeleno

        fillUserLabel(currentUserLabel);
    }

    @FXML
    public void onClear(){
        viewHeadphoneName1.setText("Name*");
        viewHeadphoneName2.setText("Name*");
        headphones1ImageView.setImage(null);
        headphones2ImageView.setImage(null);
        headphonesForComparing1=null;
        headphonesForComparing2=null;
        clearHeadphonesGrid(nameLabel1, yearLabel1, impedanceLabel1, minFreqLabel1, maxFreqLabel1, batteryLabel1, priceLabel1, descLabel1, brandLabel1, techLabel1, usesLabel1, typesLabel1);
        clearHeadphonesGrid(nameLabel2, yearLabel2, impedanceLabel2, minFreqLabel2, maxFreqLabel2, batteryLabel2, priceLabel2, descLabel2, brandLabel2, techLabel2, usesLabel2, typesLabel2);
    }

    private void clearHeadphonesGrid(Label nameLabel, Label yearLabel, Label impedanceLabel, Label minFreqLabel, Label maxFreqLabel, Label batteryLabel, Label priceLabel, Label descLabel, Label brandLabel, Label techLabel, Label usesLabel, Label typesLabel) {
        nameLabel.setText("");
        yearLabel.setText("");
        impedanceLabel.setText("");
        minFreqLabel.setText("");
        maxFreqLabel.setText("");
        priceLabel.setText("");
        batteryLabel.setText("");
        descLabel.setText("");
        brandLabel.setText("");
        techLabel.setText("");
        usesLabel.setText("");
        typesLabel.setText("");
    }

    @FXML
    public void onCompare(){
        if(headphonesForComparing1==null || headphonesForComparing2==null){
            AlertUtil.showErrorAlert("Both headphones must be chosen to start comparison");
            return;
        }

        showDetailsForHeadphones(headphonesForComparing1, nameLabel1, yearLabel1, impedanceLabel1, minFreqLabel1, maxFreqLabel1, batteryLabel1, priceLabel1, descLabel1, brandLabel1, techLabel1, usesLabel1, typesLabel1);
        showDetailsForHeadphones(headphonesForComparing2, nameLabel2, yearLabel2, impedanceLabel2, minFreqLabel2, maxFreqLabel2, batteryLabel2, priceLabel2, descLabel2, brandLabel2, techLabel2, usesLabel2, typesLabel2);
    }

    private void showDetailsForHeadphones(Headphones headphonesForComparing, Label nameLabel, Label yearLabel, Label impedanceLabel, Label minFreqLabel, Label maxFreqLabel, Label batteryLabel, Label priceLabel, Label descLabel, Label brandLabel, Label techLabel, Label usesLabel, Label typesLabel) {
        nameLabel.setText(headphonesForComparing.getName()!=null?headphonesForComparing.getName():"N/A");
        yearLabel.setText(headphonesForComparing.getYearEdition() + " "); // " " da ga prebaci u tekstu ondah a ne da registrira kao broj
        impedanceLabel.setText(headphonesForComparing.getImpedance() + " ");
        minFreqLabel.setText(headphonesForComparing.getMinFrequency() + " ");
        maxFreqLabel.setText(headphonesForComparing.getMaxFrequency() + " ");
        batteryLabel.setText(headphonesForComparing.getBatteryLife() + " ");
        priceLabel.setText(headphonesForComparing.getPrice() + " ");
        descLabel.setText(headphonesForComparing.getDescription()!=null?headphonesForComparing.getDescription():"N/A");
        brandLabel.setText(headphonesForComparing.getBrand()!=null?headphonesForComparing.getBrand().getName():"N/A");
        techLabel.setText(headphonesForComparing.getTechnologies()!=null? listTech(headphonesForComparing):"N/A");
        usesLabel.setText(headphonesForComparing.getPossibleUsages()!=null?listUses(headphonesForComparing):"N/A");
        typesLabel.setText(headphonesForComparing.getType()!=null?headphonesForComparing.getType().getName():"N/A");
    }

    private String listUses(Headphones headphonesForComparing) {
        return headphonesForComparing.getPossibleUsages()
                .stream()
                .map(Use::getName)
                .collect(Collectors.joining(", "));
    }

    private String listTech(Headphones headphonesForComparing) {
        return headphonesForComparing.getTechnologies()
                .stream()
                .map(Technology::getName)
                .collect(Collectors.joining(", "));
    }


    private void setupDropZones() {
        setInRange();
        setOutOfRange();
        setOnDrop();
    }

    private void setOutOfRange() {
        dropZone1.setOnDragExited(event-> dropZone1.setStyle("-fx-border-color: #f4f4f4;"));
        dropZone2.setOnDragExited(event ->dropZone2.setStyle("-fx-border-color: #f4f4f4"));
    }

    private void setOnDrop() {
        dropZone1.setOnDragDropped(event->{
            Dragboard db = event.getDragboard();
            boolean success = false;
            if(db.hasString()){
                int id = Integer.parseInt(db.getString());
                headphonesService.getHeadphonesById(id)
                        .ifPresent(h-> selectedHeadphones =h); // jer servis vraca optional
             displayHeadphonesVBox(dropZone1, viewHeadphoneName1, headphones1ImageView);
             headphonesForComparing1=selectedHeadphones;
             success=true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

        dropZone2.setOnDragDropped(event ->{
           Dragboard db = event.getDragboard();
           boolean success=false;
           if(db.hasString()){
               int id = Integer.parseInt(db.getString());
               headphonesService.getHeadphonesById(id)
                       .ifPresent(h->selectedHeadphones=h);

               displayHeadphonesVBox(dropZone2, viewHeadphoneName2, headphones2ImageView);
               headphonesForComparing2=selectedHeadphones;
               success=true;
           }
           event.setDropCompleted(success);
           event.consume();
        });
    }

    private void displayHeadphonesVBox(VBox dropZone, Label viewHeadphoneName, ImageView headphonesImageView) {
        dropZone.setStyle("-fx-border-color: #00ff4d");
        viewHeadphoneName.setText(selectedHeadphones.getName()!=null?selectedHeadphones.getName():"N/A");
        if(selectedHeadphones.getImagePath()!=null)setImage(selectedHeadphones.getImagePath(),headphonesImageView);
        else MediaUtil.showPlaceholder(headphonesImageView);
    }

    private void setImage(String imagePath, ImageView headphonesImageView) {
        if(imagePath==null || imagePath.isEmpty()){
            showPlaceHolderImage(headphonesImageView);
            return;
        }
        try{
            File imageFile = new File(imagePath);
            if(!imageFile.exists()){
                showPlaceHolderImage(headphonesImageView);
                return;
            }
            Image image = new Image(new FileInputStream(imageFile));
            headphonesImageView.setImage(image);
        }
        catch (Exception _){
            showPlaceHolderImage(headphonesImageView);
        }
    }

    private void showPlaceHolderImage(ImageView headphonesImageView) {
        try{
            var placeholder = getClass().getResourceAsStream("/hr/algebra/assets/No_Image_Available.jpg");
            if(placeholder!=null){
                headphonesImageView.setImage(new Image(placeholder));
            }
        }
        catch (Exception _){
            AlertUtil.showErrorAlert("Cannot load any images");
        }
    }

    private void setInRange() {
        dropZone1.setOnDragOver(event->{
            event.acceptTransferModes(TransferMode.COPY); //drop nece radit bez ovog
            dropZone1.setStyle("-fx-border-color: #0390fc;");
            event.consume();
        });
        dropZone2.setOnDragOver(event->{
            event.acceptTransferModes(TransferMode.COPY); //drop nece radit bez ovog
            dropZone2.setStyle("-fx-border-color: #0390fc;");
            event.consume();
        });
    }


    private void setUpDragSource() {
        headphonesListView.setOnDragDetected(event->{
           selectedHeadphones =headphonesListView.getSelectionModel().getSelectedItem();
           if(selectedHeadphones ==null){
               return;
           }

            Dragboard db= headphonesListView.startDragAndDrop(TransferMode.COPY);

            ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(selectedHeadphones.getId()));
            db.setContent(content);

            event.consume();
        });
    }

    private void initListView() {
        List<Headphones>allHeadphones=headphonesService.getAllHeadphones();
        headphonesListView.setItems(FXCollections.observableArrayList(allHeadphones));
        headphonesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        headphonesListView.setCellFactory(lv->new ListCell<Headphones>(){
            @Override
            protected void updateItem(Headphones h, boolean empty){
                super.updateItem(h,empty);
                setText(empty ||h==null?null:h.getName());
            }
        });
    }

    @Override
    protected Node getMainNode() {
        return nameLabel1;
    }
}
