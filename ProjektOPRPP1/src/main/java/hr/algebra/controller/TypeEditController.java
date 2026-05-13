package hr.algebra.controller;
import hr.algebra.models.Type;
import hr.algebra.services.TypeService;
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
        if(currentUser==null){
            showAlert(Alert.AlertType.ERROR, "Stop there", "One must be logged in to edit");
            return;
        }

        if(nameField.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Error", "Name cannot be empty");
            return;
        }
        if(selectedType==null){ // trenutno ne postoji type s tim imenom pa ga dodaj
            typeService.save(new Type(nameField.getText()));
            log("INSERT", "Type", nameField.getText());
            statusLabel.setText("Type succesfully inserted");
            statusLabel.setTextFill(Color.GREEN);
        }
        else{ //postoji type s tim imenom pa ga updateaj
            selectedType.setName(nameField.getText());
            typeService.save(selectedType);
            log("UPDATE", "Type", selectedType.getName());
            statusLabel.setText("Type succesfully updated");
            statusLabel.setTextFill(Color.GREEN);
        }

        showAlert(Alert.AlertType.INFORMATION, "Success", "Type has been successfully saved");
    }

    @Override
    protected Node getMainNode() {
        return nameField;
    }
}
