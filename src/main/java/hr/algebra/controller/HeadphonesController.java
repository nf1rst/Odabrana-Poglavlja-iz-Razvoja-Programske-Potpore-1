package hr.algebra.controller;

import hr.algebra.models.*;
import hr.algebra.services.BrandService;
import hr.algebra.services.HeadphonesService;
import hr.algebra.services.TypeService;
import hr.algebra.util.*;
import hr.algebra.xml.export.XMLExporter;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.stream.Collectors;

public class HeadphonesController extends BaseController {
    @FXML private TableView<Headphones> headphonesTable;
    @FXML private TableColumn<Headphones, String> colName;
    @FXML private TableColumn<Headphones, String> colBrand;
    @FXML private TableColumn<Headphones, String> colType;
    @FXML private TableColumn<Headphones, Number> colYear;
    @FXML private TableColumn<Headphones, Number> colPrice;
    @FXML private TableColumn<Headphones, Number> colBattery;
    @FXML private TableColumn<Headphones,  Number> colImpedance;

    @FXML private ImageView headphoneImageView;
    @FXML private Label nameLabel;
    @FXML private Label yearLabel;
    @FXML private Label priceLabel;
    @FXML private Label impedanceLabel;
    @FXML private Label minFreqLabel;
    @FXML private Label maxFreqLabel;
    @FXML private Label batteryLabel;
    @FXML private Label brandLabel;
    @FXML private Label typeLabel;
    @FXML private Label descLabel;
    @FXML private Label techLabel;
    @FXML private Label useLabel;

    @FXML private TextField searchField;
    @FXML private ComboBox<Brand> filterBrandCombo;
    @FXML private ComboBox<Type> filterTypeCombo;

    private final HeadphonesService headphonesService = HeadphonesService.getInstance();
    private final BrandService brandService = BrandService.getInstance();
    private final TypeService typeService = TypeService.getInstance();

    private Headphones selectedHeadphnes = null;

    @FXML private Label currentUserLabel;

    @FXML
    public void initialize(){
        setupColums();
        setupTableSelection();
        setUpComboBoxes();
        setupSearch();
        fillUserLabel(currentUserLabel);
        fillTable(headphonesTable, headphonesService.getAllHeadphones());
    }

    private void setUpComboBoxes() {
        List<Type>allTypes=typeService.getAllTypes();
        List<Brand>allBrands = brandService.getAllBrands();

        filterTypeCombo.setItems(FXCollections.observableArrayList(allTypes));
        filterBrandCombo.setItems(FXCollections.observableArrayList(allBrands));

        filterBrandCombo.setConverter(new StringConverter<Brand>() {
            @Override
            public String toString(Brand brand) {
                return brand.getName()!=null?brand.getName():null;
            }

            @Override
            public Brand fromString(String s) {
                return null;
            }
        });

        filterTypeCombo.setConverter(new StringConverter<Type>() {
            @Override
            public String toString(Type type) {
                return type.getName()!=null?type.getName():null;
            }

            @Override
            public Type fromString(String s) {
                return null;
            }
        });

        filterBrandCombo
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, old, val)->{
                    if(val!=null){
                        fillTable(headphonesTable, headphonesService.getByBrand(val.getId()));
                        filterTypeCombo.getSelectionModel().clearSelection();
                    }
                });

        filterTypeCombo
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, old, val)->{
                    if(val!=null){
                        fillTable(headphonesTable, headphonesService.getByType(val.getId()));
                        filterBrandCombo.getSelectionModel().clearSelection();
                    }
                });
    }


    private void setupSearch() {
        searchField.textProperty().addListener((obs, old, q)->fillTable(headphonesTable, headphonesService.search(q)));//ovaj vraca ili sve ili samo filtrirane ako je query validan
    }

    private void setupTableSelection() {
        headphonesTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs,old, val)->{
                    if(val!=null){
                        selectedHeadphnes=val;
                        populateDetailsForm(val);
                    }
                });
    }

    private void populateDetailsForm(Headphones h) {
        nameLabel.setText(h.getName());
        yearLabel.setText(Integer.toString(h.getYearEdition()));
        priceLabel.setText((h.getPrice()) + "€");
        impedanceLabel.setText((h.getImpedance())+" Ω");
        minFreqLabel.setText((h.getMinFrequency())+" Hz");
        maxFreqLabel.setText((h.getMaxFrequency())+" Hz");
        batteryLabel.setText((h.getBatteryLife()==0?"Wired":h.getBatteryLife() + " h")); //bez ovog "h" bi on to shvatio kao int
        brandLabel.setText(h.getBrand().getName());
        typeLabel.setText(h.getType().getName());
        descLabel.setText(h.getDescription());

        setImage(h.getImagePath());

        techLabel.setText( // jer nema pametnijeg nacina za iscupat samo name iz techonologies
                h.getTechnologies()
                        .stream()
                        .map(Technology::getName)
                        .collect(Collectors.joining(", "))
        );

        useLabel.setText(
                h.getPossibleUsages()
                        .stream()
                        .map(Use::getName)
                        .collect(Collectors.joining(","))
        );
    }

//https://stackoverflow.com/questions/20547560/how-to-set-value-to-cell-in-tableview-in-javafx
    private void setupColums(){
        colName.setCellValueFactory(
                d->new SimpleStringProperty(d.getValue().getName()));
        colBrand.setCellValueFactory(
                d->new SimpleStringProperty(d.getValue().getBrand().getName()));
        colType.setCellValueFactory(
                d->new SimpleStringProperty(d.getValue().getType()!=null?
                        d.getValue().getType().getName()
                        : ""));
        colYear.setCellValueFactory(
                d-> new SimpleIntegerProperty(d.getValue().getYearEdition()));
        //bez ovog asString na kraju ne radi
        colPrice.setCellValueFactory(
                d->new SimpleDoubleProperty(d.getValue().getPrice()));
        colBattery.setCellValueFactory(
                d->new SimpleIntegerProperty(d.getValue().getBatteryLife()));
        colImpedance.setCellValueFactory(
                d->new SimpleIntegerProperty(d.getValue().getImpedance()));
    }

    @FXML
    public void onEdit(){
        if(!Session.userLoggedIn()){
            AlertUtil.showErrorAlert("One must be logged in to edit");
            return;
        }
        SceneLoadingUtil.loadNewCustomWindow("/hr/algebra/views/insert-update-headphones-view.fxml", "Insert and Update Headphone", getMainNode(), 600,600);
    }

    @FXML
    public void onDelete(){
        if(!Session.userCanEdit()){
            AlertUtil.showErrorAlert("One must be logged in to edit");
            return;
        }
        if(selectedHeadphnes==null){
            AlertUtil.showErrorAlert("Please select headphones to delete");

        }

        if(!AlertUtil.showConfirmationAlert("Do you want to delete headphones: " + selectedHeadphnes.getName())) return;

        headphonesService.delete(selectedHeadphnes.getId());

        LoggingUtil.logInfo( "DELETE", "Headphones", selectedHeadphnes.getName(),Session.checkCurrentUserName());
        XMLLoggingUtil.log("INFO", "DELETE", "successfully deleted", "headphones", Session.checkCurrentUserName(),  selectedHeadphnes.getName());

        fillTable(headphonesTable, headphonesService.getAllHeadphones());
        selectedHeadphnes=null;
    }

    @FXML
    public void onShowAll() {
        searchField.clear();
        filterBrandCombo.setValue(null);
        filterTypeCombo.setValue(null);
        fillTable(headphonesTable, headphonesService.getAllHeadphones());
    }

     @FXML
     public void onExportXml(){
        if(selectedHeadphnes==null){
            AlertUtil.showWarningAlert("Please choose headphones to export");
            return;
        }

        Task<Void>exportTask = new Task<>(){
            @Override
            protected Void call() throws Exception {
                XMLExporter.marshallSelected(selectedHeadphnes);
                LoggingUtil.logInfo("Headphones in task export");
                return null;
            }
        };


        exportTask.setOnSucceeded(e->{
            AlertUtil.showInformationAlert("Headphones exported successfully");
            LoggingUtil.logInfo("Headphones export success");
            XMLLoggingUtil.log("INFO", "EXPORT", "successfully exported selected headphones", "headphones", Session.checkCurrentUserName(),  selectedHeadphnes.getName());
        });

        exportTask.setOnFailed(e-> {
            AlertUtil.showErrorAlert("Cannot export to xml");
            LoggingUtil.logError("XML export error", exportTask.getException());
        });

        Thread bgThread = new Thread(exportTask);
        bgThread.setDaemon(true);
        bgThread.start();
     }

    @Override
    protected Node getMainNode() {
        return searchField;
    }

        @FXML
    public void onClear(){
        clearForm();
        selectedHeadphnes=null;
    }

    private void clearForm() {
        nameLabel.setText("Name*");
        yearLabel.setText("Year*");
        priceLabel.setText("Price*");
        impedanceLabel.setText("Impedance*");
        minFreqLabel.setText("Min. frequency*");
        maxFreqLabel.setText("Max. frequency*");
        batteryLabel.setText("Battery Life(h, 0=wired)*");
        typeLabel.setText("Type*");
        brandLabel.setText("Brand*");
        techLabel.setText("Technologies*");
        useLabel.setText("Uses*");
        descLabel.setText("Description*");

        MediaUtil.showPlaceholder(headphoneImageView);
        headphonesTable.getSelectionModel().clearSelection();
    }
    private void setImage(String path) {
        if(path==null || path.isEmpty()){
            MediaUtil.showPlaceholder(headphoneImageView);
            return;
        }
        try{
            File imageFIle = new File(path);
            if(!imageFIle.exists()){
                MediaUtil.showPlaceholder(headphoneImageView);
                return;
            }
            MediaUtil.showImage(headphoneImageView, imageFIle.toString());
//            Image image = new Image(new FileInputStream(imageFIle));
//            headphoneImageView.setImage(image);
        }
        catch (Exception _){
            MediaUtil.showPlaceholder(headphoneImageView);
        }
    }
}
