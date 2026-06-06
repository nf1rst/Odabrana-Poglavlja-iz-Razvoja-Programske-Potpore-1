package hr.algebra.controller;

import hr.algebra.exceptions.CannotPerformActionException;
import hr.algebra.models.*;
import hr.algebra.services.*;
import hr.algebra.util.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import java.util.List;
import java.util.Optional;

public class HeadphonesEditController extends BaseController{
    @FXML private TextField nameField;
    @FXML private TextField yearField;
    @FXML private TextField priceField;
    @FXML private TextField impedanceField;
    @FXML private TextField minFreqField;
    @FXML private TextField maxFreqField;
    @FXML private TextField batteryField;
    @FXML private TextField descField;
    @FXML private ComboBox<Brand> brandComboBox;
    @FXML private ComboBox<Type>typeComboBox;
    @FXML private ListView<Technology> technologiesListView;
    @FXML private ListView<Use>usesListView;
    @FXML private Label imageLabel;

    @FXML private Label statusLabel;

    private final HeadphonesService headphonesService=HeadphonesService.getInstance();
    private final BrandService brandService=BrandService.getInstance();
    private final TypeService typeService=TypeService.getInstance();
    private final TechnologyService technologyService=TechnologyService.getInstance();
    private final UseService userService = UseService.getInstance();

    private final ContextMenu suggestions= new ContextMenu();
    private Headphones selectedHeadphones;
    private String selectedImagePath=null;

    @FXML
    public void initialize(){
        nameField.textProperty().addListener((obs, old, val)->{
            if(val==null ||val.isBlank()){
                suggestions.hide();
                return;
            }
            showSuggestions(val);
        });

        initComboBoxes();

        initListViews();
    }

    private void initListViews() {
        List<Technology>allTechnologies=technologyService.getAllTechnologies();
        List<Use>allUses=userService.getAllUses();

        technologiesListView.setItems(FXCollections.observableArrayList(allTechnologies));
        technologiesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        usesListView.setItems(FXCollections.observableArrayList(allUses));
        usesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        technologiesListView.setCellFactory(lv->new ListCell<Technology>(){
            @Override
            protected void updateItem(Technology t, boolean empty){
                super.updateItem(t,empty);
                setText(empty || t==null?null:t.getName());
            }
        });

        usesListView.setCellFactory(lv->new ListCell<Use>(){
            @Override
            protected void updateItem(Use u, boolean emtpy){
                super.updateItem(u,emtpy);
                setText(emtpy|| u==null?null:u.getName());
            }
        });

    }

    private void initComboBoxes() {
        List<Brand>allBrands=brandService.getAllBrands();
        List<Type>allTypes=typeService.getAllTypes();

        brandComboBox.setItems(FXCollections.observableArrayList(allBrands));
        typeComboBox.setItems(FXCollections.observableArrayList(allTypes));

        //https://webtechie.be/post/2025-04-09-javafx-combobox-with-objects/
        //tutorial za prikazivanje samo imena a ne cijelog objekta u Combo box

        brandComboBox.setConverter(new StringConverter<Brand>() {
            @Override
            public String toString(Brand brand) {
                return brand!=null ? brand.getName():"";
            }

            @Override
            public Brand fromString(String s) { // trenutno nebitno za display pa moze osta
                return null;
            }
        });

        typeComboBox.setConverter(new StringConverter<Type>() {
            @Override
            public String toString(Type type) {
                return type!=null?type.getName():"";
            }

            @Override
            public Type fromString(String s) {
                return null;
            }
        });
    }

    private void showSuggestions(String val) {
        suggestions.getItems().clear();
        List<Headphones>allHeadphones;
        allHeadphones=headphonesService.getAllHeadphones();

        allHeadphones
                .stream()
                .filter(h->h.getName().startsWith(val))
                .limit(5)
                .forEach(h->{
                    MenuItem item = new MenuItem(h.getName());
                    item.setOnAction(e->{
                        selectedHeadphones=h;
                        fillHeadphonesDetails(h);
                    });
                    suggestions.getItems().add(item);
                });
        if(!suggestions.getItems().isEmpty()){
            suggestions.show(nameField, Side.BOTTOM, 0,0);
        }
        else{
            suggestions.hide();
        }
    }

    private void fillHeadphonesDetails(Headphones h) {
        nameField.setText(h.getName());
        yearField.setText(Integer.toString(h.getYearEdition()));
        priceField.setText(Double.toString(h.getPrice()));
        impedanceField.setText(Integer.toString(h.getImpedance()));
        minFreqField.setText(Integer.toString(h.getMinFrequency()));
        maxFreqField.setText(Integer.toString(h.getMaxFrequency()));
        batteryField.setText(Integer.toString(h.getBatteryLife()));
        brandComboBox.setValue(h.getBrand());
        typeComboBox.setValue(h.getType());
        descField.setText(h.getDescription());

        technologiesListView.getSelectionModel().clearSelection();
        if(h.getTechnologies()!=null){
            h.getTechnologies().forEach(t->technologiesListView.getSelectionModel().select(t));
        }

        usesListView.getSelectionModel().clearSelection();
        if(h.getPossibleUsages()!=null){
            h.getPossibleUsages().forEach(u->usesListView.getSelectionModel().select(u));
        }
    }

    @FXML
    public void onBack(){
        SceneLoadingUtil.loadNewWindow("/hr/algebra/views/headphones-view.fxml", "Headphones view", getMainNode());
    }

    @FXML
    public void onSave(){
        if(!Session.userCanEdit()){
            AlertUtil.showErrorAlert( "Admin does not allow editing at the moment");
            return;
        }

        if(!validateUserInput()){
            AlertUtil.showErrorAlert("Values you've entered are not valid, please retry");
            return;
        }
        
        trySaveHeadphones();
    }

    private void trySaveHeadphones() {
        try{
            List<Use>userSelectedUses=usesListView.getSelectionModel().getSelectedItems();
            List<Technology>userselectedTech=technologiesListView.getSelectionModel().getSelectedItems();

            if(!headphonesService.exists(nameField.getText())){ //
                insertNewHeadphones(userselectedTech, userSelectedUses);
                fillStatusLabel(statusLabel,"Headphones successfully inserted", Color.GREEN);
            }
            else{
                updateExistingHeadphones(userselectedTech, userSelectedUses);
                fillStatusLabel(statusLabel,"Headphones successfully updated", Color.BLUE);
            }

        }
        catch(NumberFormatException _){
            AlertUtil.showErrorAlert( "Year, price, impedance and frequency fields must be numbers");
        }
    }

    private void updateExistingHeadphones(List<Technology> userselectedTech, List<Use> userSelectedUses) {
        selectedHeadphones.setYearEdition(Integer.parseInt(yearField.getText()));
        selectedHeadphones.setImpedance(Integer.parseInt(impedanceField.getText()));
        selectedHeadphones.setMinFrequency(Integer.parseInt(minFreqField.getText()));
        selectedHeadphones.setMaxFrequency(Integer.parseInt(maxFreqField.getText()));
        selectedHeadphones.setBatteryLife(Integer.parseInt(batteryField.getText()));
        selectedHeadphones.setPrice(Double.parseDouble(priceField.getText()));
        selectedHeadphones.setDescription(descField.getText());
        selectedHeadphones.setBrand(brandComboBox.getValue());
        selectedHeadphones.setType(typeComboBox.getValue());
        selectedHeadphones.setImagePath(selectedImagePath);

        headphonesService.save(selectedHeadphones);
        updateManyToMany(selectedHeadphones, userselectedTech, userSelectedUses); // posebne tablice u bazi pa se mora rucno
        // sad zbog onog scope identity ovdje se moze napravit getID da vrati pravi ID koji mu je baza generirala
        LoggingUtil.logInfo("UPDATE", "Headphones", selectedHeadphones.getName(), Session.checkCurrentUserName());
        XMLLoggingUtil.log("INFO", "UPDATE", "successfully updated", "headphones", Session.checkCurrentUserName(),  nameField.getText());
    }

    private void insertNewHeadphones(List<Technology> userselectedTech, List<Use> userSelectedUses) {
        Headphones headphonesForInsert = new Headphones(
                nameField.getText(),
                Integer.parseInt(yearField.getText()),
                Integer.parseInt(impedanceField.getText()),
                Integer.parseInt(minFreqField.getText()),
                Integer.parseInt(maxFreqField.getText()),
                Integer.parseInt(batteryField.getText()),
                Double.parseDouble(priceField.getText()),
                descField.getText(),
                selectedImagePath,
                brandComboBox.getValue(),
                typeComboBox.getValue(),
                userselectedTech,
                userSelectedUses
        );
        headphonesService.save(headphonesForInsert);
        updateManyToMany(headphonesForInsert, userselectedTech, userSelectedUses);
        selectedHeadphones=headphonesForInsert;

        LoggingUtil.logInfo("INSERT", "Headphones", headphonesForInsert.getName(), Session.checkCurrentUserName());
        XMLLoggingUtil.log("INFO", "INSERT", "successfully inserted", "headphones", Session.checkCurrentUserName(), nameField.getText());
    }


    private void updateManyToMany(Headphones selectedHeadphones, List<Technology> userselectedTech, List<Use> userSelectedUses) {
        //ako postoje vec neke veze u bazi, trebaju se zbrisat prije nego se krene ubacivat

        //!! ovdje ne moze ubacit jer ne zna koji identity ID dobiju novoubacene slusalice

        try {
            if(selectedHeadphones.getTechnologies()!=null){
                headphonesService.removeAllTechnologiesForHeadphones(selectedHeadphones.getId());
            }
            if(selectedHeadphones.getPossibleUsages()!=null){
                headphonesService.removeAllUsesForHeadphones(selectedHeadphones.getId());
            }

            for(Technology t:userselectedTech){
                headphonesService.addTechnology(selectedHeadphones.getId(),t.getId());
            }
            for(Use u:userSelectedUses){
                headphonesService.addUse(selectedHeadphones.getId(),u.getId());
            }
        }
        catch (CannotPerformActionException e){
            AlertUtil.showErrorAlert(e.getMessage());
        }
    }

    private boolean validateUserInput() {
        if(nameField.getText().isEmpty()){
            AlertUtil.showErrorAlert( "Name cannot be empty");
            return false;
        }
        if(priceField.getText().isEmpty()){
            AlertUtil.showErrorAlert( "Price cannot be empty");
            return false;
        }
        if(brandComboBox.getValue()==null){
            AlertUtil.showErrorAlert( "Brand cannot be empty");
            return false;
        }
        if(typeComboBox.getValue()==null){
            AlertUtil.showErrorAlert( "Type cannot be empty");
            return false;
        }

        try{
            Integer.parseInt(yearField.getText());
            Integer.parseInt(impedanceField.getText());
            Integer.parseInt(minFreqField.getText());
            Integer.parseInt(maxFreqField.getText());
            Integer.parseInt(batteryField.getText());
            Double.parseDouble(priceField.getText());
        }
        catch (NumberFormatException _){
            return false;
        }
        return true;
    }

    @FXML
    public void onChooseImage(){
        try{
            Optional<String> path = MediaUtil.chooseFile();
            if(path.isPresent()) selectedImagePath=path.get();
        }
        catch (Exception _){
            AlertUtil.showErrorAlert( "Cannot copy image you've selected");
        }
        imageLabel.setText("Image selected");
    }

    @Override
    protected Node getMainNode() {
        return nameField;
    }
}
