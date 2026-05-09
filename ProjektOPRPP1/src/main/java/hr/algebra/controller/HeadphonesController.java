package hr.algebra.controller;
import hr.algebra.models.*;
import hr.algebra.services.*;
import hr.algebra.xml.ActionLogger;
import hr.algebra.xml.export.HeadphonesCatalog;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import java.io.File;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;


public class HeadphonesController extends BaseController {
    @FXML private TableView<Headphones> headphonesTable;
    @FXML private TableColumn<Headphones, String> colName;
    @FXML private TableColumn<Headphones, String> colBrand;
    @FXML private TableColumn<Headphones, String> colType;
    @FXML private TableColumn<Headphones, Number> colYear;
    @FXML private TableColumn<Headphones, Number> colPrice;
    @FXML private TableColumn<Headphones, Number> colBattery;
    @FXML private TableColumn<Headphones,  Number> colImpedance;

    @FXML private TextField nameField;
    @FXML private TextField yearField;
    @FXML private TextField priceField;
    @FXML private TextField impedanceField;
    @FXML private TextField minFreqField;
    @FXML private TextField maxFreqField;
    @FXML private TextField batteryField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<Brand>brandCombo;
    @FXML private ComboBox<Type>typeCombo;
    @FXML private ListView<Technology> technologiesListView;
    @FXML private ListView<Use>usesListView;
    @FXML private Label imageLabel;

    @FXML private TextField searchField;
    @FXML private ComboBox<Brand> filterBrandCombo;
    @FXML private ComboBox<Type> filterTypeCombo;

    private final HeadphonesService headphonesService = HeadphonesService.getInstance();
    private final BrandService brandService = BrandService.getInstance();
    private final TypeService typeService = TypeService.getInstance();
    private final TechnologyService technologyService = TechnologyService.getInstance();
    private final UseService useService = UseService.getInstance();

    private Headphones selectedHeadphnes = null;
    private String selectedImagePath=null;

    @FXML
    public void initialize(){
        // je ltu treba bit metoda da poveca nesto iz vanjskog
        setupColums();
        setupCombos();
        setupListViews();
        setupTableSelection();
        setupSearch();
        refresh();
    }

    private void refresh() {
        headphonesTable.setItems(FXCollections.observableArrayList(headphonesService.getAllHeadphones()));
        headphonesTable.getSelectionModel().clearSelection();
    }

    private void setupSearch() {
        searchField.textProperty().addListener((obs, old, q)->{
            if(q==null || q.trim().isEmpty()){
                refresh();
            }
            else{
                headphonesTable.setItems(FXCollections.observableArrayList(headphonesService.search(q)));
            }
        });
    }

    private void setupTableSelection() {
        headphonesTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs,old, val)->{
                    if(val!=null){
                        selectedHeadphnes=val;
                        populateForm(val);
                    }
                });
    }

    private void populateForm(Headphones h) {
        nameField.setText(h.getName());
        yearField.setText(String.valueOf(h.getYearEdition()));
        priceField.setText(String.valueOf(h.getPrice()));
        impedanceField.setText(String.valueOf(h.getImpedance()));
        minFreqField.setText(String.valueOf(h.getMinFrequency()));
        maxFreqField.setText(String.valueOf(h.getMaxFrequency()));
        batteryField.setText(String.valueOf(h.getBatteryLife()));
        brandCombo.setValue(h.getBrand());
        typeCombo.setValue(h.getType());
        selectedImagePath=h.getImagePath();
        imageLabel.setText(h.getImagePath()!=null?h.getImagePath():"No picture to display");

        technologiesListView.getSelectionModel().clearSelection();
        if(h.getTechnologies()!=null){
            h.getTechnologies().forEach(t->technologiesListView.getSelectionModel().select(t));
        }

        usesListView.getSelectionModel().clearSelection();
        if(h.getPossibleUsages()!=null){
            h.getPossibleUsages().forEach(u->usesListView.getSelectionModel().select(u));
        }
    }

    private void setupListViews() {
        technologiesListView.setItems(
                FXCollections.observableArrayList(technologyService.getAllTechnologies()));
        technologiesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //da se vide samo nazivi u listView
        technologiesListView.setCellFactory(lv->new ListCell<Technology>(){
            protected void updateItem(Technology t, boolean empty) {
                super.updateItem(t,empty);
                setText(empty || t==null?null:t.getName());
            }
        });

        usesListView.setItems(FXCollections.observableArrayList(useService.getAllUses()));
        usesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        usesListView.setCellFactory(lv->new ListCell<Use>(){
            protected void updateItem(Use u, boolean empty){
                super.updateItem(u,empty);
                setText(empty || u==null?null:u.getName());
            }
        });
    }

    private void setupColums(){
        colName.setCellValueFactory(
                d->new SimpleStringProperty(d.getValue().getName()));
        colBrand.setCellValueFactory(
                d->new SimpleStringProperty(d.getValue().getName()));
        colType.setCellValueFactory(
                d->new SimpleStringProperty(d.getValue().getType()!=null?
                        d.getValue().getName()
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

    public void setupCombos(){
        List<Brand> brands = brandService.getAllBrands();
        List<Type> types= typeService.getAllTypes();

        brandCombo.setItems(FXCollections.observableList(brands));
        typeCombo.setItems(FXCollections.observableList(types));

        brandCombo.setConverter(new StringConverter<Brand>() {
            @Override
            public String toString(Brand brand) {return brand!=null? brand.getName():"";}
            @Override
            public Brand fromString(String s) {return null;}
        });
        typeCombo.setConverter(new StringConverter<Type>() {
            @Override
            public String toString(Type type) {return type!=null?type.getName():"";}
            @Override
            public Type fromString(String s) {return null;}
        });

        filterBrandCombo.getItems().add(null);
        filterBrandCombo.getItems().addAll(brands);
        filterBrandCombo.setConverter(new StringConverter<Brand>() {
            @Override
            public String toString(Brand brand) {
                return brand!=null? brand.getName():"All brands";
            }
            @Override
            public Brand fromString(String s) {
                return null;
            }
        });
        filterBrandCombo.setOnAction(e->OnFilterBrand());

        filterTypeCombo.getItems().add(null);
        filterTypeCombo.getItems().addAll(types);
        filterTypeCombo.setConverter(new StringConverter<Type>() {
            @Override
            public String toString(Type type) {
                return type!=null?type.getName():"All types";
            }
            @Override
            public Type fromString(String s) {
                return null;
            }
        });
        filterTypeCombo.setOnAction(e->onFilterType());
    }


    @FXML
    public void onSave(){
        if(!validateForm())return;

        try{
            boolean isNew=(selectedHeadphnes==null);
            if(isNew){ // ako ne posoji napravi novi
                selectedHeadphnes= new Headphones(
                        nameField.getText(),
                        Integer.parseInt(yearField.getText()),
                        Integer.parseInt(impedanceField.getText()),
                        Integer.parseInt(minFreqField.getText()),
                        Integer.parseInt(maxFreqField.getText()),
                        Integer.parseInt(batteryField.getText()),
                        Double.parseDouble(priceField.getText()),
                        descriptionArea.getText(),
                        selectedImagePath,
                        brandCombo.getValue(),
                        typeCombo.getValue(),
                        technologiesListView.getSelectionModel().getSelectedItems(),
                        usesListView.getSelectionModel().getSelectedItems()
                );
            }
            else{//ako postoje napravi update
                selectedHeadphnes.setName(nameField.getText());
                selectedHeadphnes.setYearEdition(Integer.parseInt(yearField.getText()));
                selectedHeadphnes.setImpedance(Integer.parseInt(yearField.getText()));
                selectedHeadphnes.setMinFrequency(Integer.parseInt(minFreqField.getText()));
                selectedHeadphnes.setMaxFrequency(Integer.parseInt(maxFreqField.getText()));
                selectedHeadphnes.setPrice(Double.parseDouble(priceField.getText()));
                selectedHeadphnes.setDescription(descriptionArea.getText());
                selectedHeadphnes.setImagePath(selectedImagePath);
                selectedHeadphnes.setBrand(brandCombo.getValue());
                selectedHeadphnes.setType(typeCombo.getValue());
            }

            headphonesService.save(selectedHeadphnes);
            if (!isNew) {
                updateManyToMany(selectedHeadphnes);
            }
            ActionLogger.log(checkUsernameForLogging(), isNew?"INSERT":"UPDATE", "Headphones", selectedHeadphnes.getName());
            showAlert(Alert.AlertType.INFORMATION, "Success", "Headphones successfully saved");
            refresh();
        }
        catch (NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Error", "Check your numeric values");
        }
    }

    @FXML
    public void onDelete(){
        if(currentUser==null){
            showAlert(Alert.AlertType.ERROR, "Error", "One must be logged in to edit");
            return;
        }
        if(selectedHeadphnes.getName().isEmpty()){
            showAlert(Alert.AlertType.WARNING, "Validation", "Name field cannot be emtpy");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Do you want to delete headphones: " + selectedHeadphnes.getName());
        if(alert.showAndWait().filter(b->b==ButtonType.OK).isEmpty())return;

        deleteImageFile(selectedImagePath);
        headphonesService.delete(selectedHeadphnes.getId());
        ActionLogger.log(checkUsernameForLogging(), "DELETE", "Headphones", selectedHeadphnes.getName());

        refresh();
    }

    @FXML
    public void onChooseImage(){
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose image for headphones: ");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Pictures", "*.png", "*.jpg", "*.jpeg"));

        File file = fc.showOpenDialog(nameField.getScene().getWindow());
        if(file ==null) return;
        try{
            Path assetsDir = Paths.get("src/main/resources/hr/algebra/assets/");
            Files.createDirectories(assetsDir);
            Path dest = assetsDir.resolve(file.getName());
            Files.copy(file.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
            selectedImagePath=dest.toString();
            imageLabel.setText(file.getName());
        }
        catch(Exception e){
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot copy picture");
        }
    }

    @FXML
    public void onShowAll() {
        searchField.clear();
        filterBrandCombo.setValue(null);
        filterTypeCombo.setValue(null);
        refresh();
    }

    private void deleteImageFile(String path) {
        if(path==null || path.isEmpty())return;
        try{
            Files.deleteIfExists(Paths.get(path));
        }
        catch(Exception ignored){}
    }


    private void onFilterType() {
        Type t = filterTypeCombo.getValue();
        if(t==null){
            refresh();
            return;
        }
        headphonesTable.setItems(FXCollections.observableArrayList(headphonesService.getByType(t.getId())));
    }

    private void OnFilterBrand() {
        Brand b = filterBrandCombo.getValue();
        if(b==null){
            refresh();
            return;
        }
        headphonesTable.setItems(FXCollections.observableArrayList(headphonesService.getByBrand(b.getId())));
    }

     @FXML
     public void onExportXml(){
         FileChooser fc = new FileChooser();
         fc.setTitle("Save FXML Catalogue");
         fc.getExtensionFilters().add(
                 new FileChooser.ExtensionFilter("XML files", "*.xml"));
         fc.setInitialFileName("headphones_catalogue.xml");

         File file = fc.showSaveDialog(nameField.getScene().getWindow());
         if(file==null)return;
        try{
            List<Headphones> list= headphonesService.getAllHeadphones();
            HeadphonesCatalog catalog = new HeadphonesCatalog(list);

            JAXBContext jaxbContext=JAXBContext.newInstance(HeadphonesCatalog.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
            marshaller.marshal(catalog, file);

            ActionLogger.log(checkUsernameForLogging(), "EXPORT", "Headphones", file.getName());

            showAlert(Alert.AlertType.INFORMATION, "Success", "Catalogue has been successfully exported to "+ file.getName());
        }
        catch (JAXBException e) {
            showAlert(Alert.AlertType.ERROR, "ERROR", "Cannot export to xml");
        }
     }

    private void convertToXML(Headphones selectedHeadphnes) throws JAXBException {
        JAXBContext jaxbContext =JAXBContext.newInstance(Headphones.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(selectedHeadphnes, stringWriter);
        String xmlContent = stringWriter.toString();
        System.out.println(xmlContent);
    }

    private void updateManyToMany(Headphones h) {
        if(h.getTechnologies()!=null){
            h.getTechnologies().forEach(t->headphonesService.removeTechnology(h.getId(), h.getId()));
        }
        if(h.getPossibleUsages()!=null){
            h.getPossibleUsages().forEach(u->headphonesService.removeUse(h.getId(), u.getId()));
        }
    }

    @Override
    protected Node getMainNode() {
        return searchField;
    }


    private boolean validateForm() {
        if (nameField.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Name cannot be empty");
            return false;
        }
        if (brandCombo.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Brand cannot be empty");
            return false;
        }
        if (typeCombo.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Type cannot be empty");
            return false;
        }
        try {
            Integer.parseInt(yearField.getText());
            Double.parseDouble(priceField.getText());

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Year and price must be numbers");
            return false;
        }
        return true;
    }

        @FXML
    public void onClear(){
        clearForm();
        selectedHeadphnes=null;
    }

    private void clearForm() {
        nameField.clear(); yearField.clear(); priceField.clear();
        impedanceField.clear(); minFreqField.clear();
        maxFreqField.clear(); batteryField.clear();
        descriptionArea.clear();
        brandCombo.setValue(null); typeCombo.setValue(null);
        technologiesListView.getSelectionModel().clearSelection();
        usesListView.getSelectionModel().clearSelection();
        imageLabel.setText("No picture");
        selectedImagePath = null;
        selectedHeadphnes = null;
        headphonesTable.getSelectionModel().clearSelection();
    }



}
