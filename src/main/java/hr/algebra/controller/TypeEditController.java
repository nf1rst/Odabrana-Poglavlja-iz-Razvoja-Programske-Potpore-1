package hr.algebra.controller;
import hr.algebra.models.Session;
import hr.algebra.models.Type;
import hr.algebra.services.TypeService;
import hr.algebra.util.AlertUtil;
import hr.algebra.util.LoggingUtil;
import hr.algebra.util.SceneLoadingUtil;
import hr.algebra.util.XMLLoggingUtil;
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
                        selectedType=t;
                        nameField.setText(t.getName());
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
    public void onBack(){
        SceneLoadingUtil.loadNewWindow("/hr/algebra/views/types-view.fxml", "Types",getMainNode());
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

        trySaveType();
    }

    private void trySaveType() {
        if(!typeService.nameExists(nameField.getText())){ // trenutno ne postoji type s tim imenom pa ga dodaj
            insertNewType();
            fillStatusLabel(statusLabel,"Type inserted successfully", Color.GREEN);
        }
        else{ //postoji type s tim imenom pa ga updateaj
            updateExistingType();
            fillStatusLabel(statusLabel,"Type updated successfully", Color.BLUE);
        }
    }

    private void updateExistingType() {
        selectedType.setName(nameField.getText());
        typeService.save(selectedType);
        LoggingUtil.logInfo("UPDATE", "Type", selectedType.getName(),Session.checkCurrentUserName());
        XMLLoggingUtil.log("INFO", "UPDATE", "successfully updated", "type", Session.checkCurrentUserName(),  nameField.getText());
    }

    private void insertNewType() {
        typeService.save(new Type(nameField.getText()));
        LoggingUtil.logInfo("INSERT", "Type", nameField.getText(), Session.checkCurrentUserName());
        XMLLoggingUtil.log("INFO", "INSERT", "successfully inserted", "type", Session.checkCurrentUserName(),  nameField.getText());
    }

    private boolean validateUserInput() {
        if(nameField.getText().isEmpty()){
            AlertUtil.showErrorAlert( "Name cannot be empty");
            return false;
        }
        return true;
    }

    @Override
    protected Node getMainNode() {
        return nameField;
    }
}
