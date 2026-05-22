package hr.algebra.controller;
import hr.algebra.models.Session;
import hr.algebra.models.Type;
import hr.algebra.services.TypeService;
import hr.algebra.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.util.List;

public class TypeEditController extends BaseController{

    @FXML private TextField nameField;
    @FXML private Label statusLabel;

    private Type selectedType;
    private List<Type>allTypes;
    private TypeService typeService=TypeService.getInstance();
    private ContextMenu suggestions = new ContextMenu();

    @FXML
    public void initialize(){
        allTypes=typeService.getAllTypes();

        nameField.textProperty().addListener((obs, old, val)->{
            if(val==null || val.isBlank()){
                suggestions.hide();
                return;
            }
            showSuggestions(val);
        });
    }

    private void showSuggestions(String val) {
        suggestions.getItems().clear();

        allTypes
                .stream()
                .filter(t->t.getName().startsWith(val))
                .limit(5)
                .forEach(t->{
                    MenuItem item = new MenuItem(t.getName());
                    item.setOnAction(e->{
                        nameField.setText(t.getName());
                        selectedType=t;
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
        if(!Session.userCanEdit()){
            AlertUtil.showErrorAlert("One must be logged in to edit");
            return;
        }

        validateUserInput();

        if(!typeService.nameExists(nameField.getText())){ // trenutno ne postoji type s tim imenom pa ga dodaj
            insertNewType();
        }
        else{ //postoji type s tim imenom pa ga updateaj
            updateExistingType();
        }

        AlertUtil.showInformationAlert( "Type has been successfully saved");
    }

    private void updateExistingType() {
        selectedType.setName(nameField.getText());
        typeService.save(selectedType);
        logger.info("UPDATE", "Type", selectedType.getName());
        statusLabel.setText("Type succesfully updated");
        statusLabel.setTextFill(Color.GREEN);
    }

    private void insertNewType() {
        typeService.save(new Type(nameField.getText()));
        logger.info("INSERT", "Type", nameField.getText());
        statusLabel.setText("Type succesfully inserted");
        statusLabel.setTextFill(Color.GREEN);
    }

    private void validateUserInput() {
        if(nameField.getText().isEmpty()){
            AlertUtil.showErrorAlert( "Name cannot be empty");
        }
    }

    @Override
    protected Node getMainNode() {
        return nameField;
    }
}
