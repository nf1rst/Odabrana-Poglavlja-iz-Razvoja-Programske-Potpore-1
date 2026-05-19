package hr.algebra.controller;

import hr.algebra.models.Technology;
import hr.algebra.services.TechnologyService;
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
                        nameField.setText(t.getName());
                        descField.setText(t.getDescription());
                        selectedTechnology=t;
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

    @FXML
    public void onSave(){
        if(currentUser==null){
            showAlert(Alert.AlertType.ERROR, "Error", "One must be logged in to edit");
            return;
        }
        
        validateUserInput();

        if(technologyService.nameExists(nameField.getText())){ //tehnologija ne postoji onda je mora ubacit
            insertTechnology();
        }
        else{// ako vec postoji onda se treba napravit update te tehnologije s istim IDjem
            updateExistingTechnology();
        }

        showAlert(Alert.AlertType.INFORMATION, "Success", "Technology has been successfully saved");
    }

    private void updateExistingTechnology() {
        selectedTechnology.setName(nameField.getText());
        selectedTechnology.setDescription(descField.getText());
        technologyService.save(selectedTechnology);
        log("UPDATE", "Technology", nameField.getText());
        statusLabel.setText("Technology succesfully updated");
        statusLabel.setTextFill(Color.GREEN);
    }

    private void insertTechnology() {
        technologyService.save(new Technology(nameField.getText(), descField.getText()));
        log("INSERT", "Technology", nameField.getText());
        statusLabel.setText("Technology succesfully inserted");
        statusLabel.setTextFill(Color.GREEN);
    }

    private void validateUserInput() {
        if(nameField.getText().isEmpty()){
            showAlert(Alert.AlertType.WARNING, "Warning", "Name must not be empty");
            return;
        }
    }

    @Override
    protected Node getMainNode() {
        return null;
    }
}
