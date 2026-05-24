package hr.algebra.controller;

import hr.algebra.models.Session;
import hr.algebra.models.Technology;
import hr.algebra.services.TechnologyService;
import hr.algebra.util.AlertUtil;
import hr.algebra.util.LoggingUtil;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.util.List;


public class TechnologyEditController extends BaseController{

    @FXML private TextField nameField;
    @FXML private TextField descField;
    @FXML private Label statusLabel;

    private Technology selectedTechnology;
    private TechnologyService technologyService= TechnologyService.getInstance();
    private final ContextMenu suggestions = new ContextMenu();
    private List<Technology> allTechnologies;

    @FXML
    public void initialize(){
        allTechnologies=technologyService.getAllTechnologies();

        nameField.textProperty().addListener((obs, old, val)->{
            if(val==null ||val.isBlank()){
                suggestions.hide();
                return;
            }
            showSuggestions(val);
        });
    }

    private void showSuggestions(String val) {
        suggestions.getItems().clear();

        allTechnologies
                .stream()
                .filter(t->t.getName().startsWith(val))
                .limit(5)
                .forEach(t->{
                    MenuItem item = new MenuItem(t.getName());
                    item.setOnAction(e->{
                        selectedTechnology=t;
                        fillTechnologyDetails(t);
                        suggestions.hide();
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

    private void fillTechnologyDetails(Technology t) {
        nameField.setText(t.getName());
        descField.setText(t.getDescription());
    }

    @FXML
    public void onSave(){
        if(!Session.userCanEdit()){
            AlertUtil.showErrorAlert("Admin does not allow editing at the moment ");
            return;
        }
        
        if(!validateUserInput()){
            return;
        }
        
        trySaveTechnology();
        selectedTechnology=null;

    }

    private void trySaveTechnology() {
            if(technologyService.nameExists(nameField.getText())){ //tehnologija ne postoji onda je mora ubacit
                insertTechnology();
                fillStatusLabel(statusLabel, "Technology successfully inserted", Color.GREEN);
            }
            else{// ako vec postoji onda se treba napravit update te tehnologije s istim IDjem
                updateExistingTechnology();
                fillStatusLabel(statusLabel, "Technology successfully updated", Color.BLUE);
            }
    }

    private void updateExistingTechnology() {
        selectedTechnology.setName(nameField.getText());
        selectedTechnology.setDescription(descField.getText());
        technologyService.save(selectedTechnology);
        LoggingUtil.logInfo("UPDATE", "Technology", nameField.getText(), Session.getCurrentUser().getUsername());
    }

    private void insertTechnology() {
        technologyService.save(new Technology(nameField.getText(), descField.getText()));
        LoggingUtil.logInfo("INSERT", "Technology", nameField.getText(), Session.getCurrentUser().getUsername());
    }

    private boolean validateUserInput() {
        if(nameField.getText().isEmpty()){
            AlertUtil.showErrorAlert( "Name must not be empty");
            return false;
        }
        return true;
    }

    @Override
    protected Node getMainNode() {
        return null;
    }
}
