package hr.algebra.controller;
import hr.algebra.models.Use;
import hr.algebra.services.UseService;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import java.util.List;


public class UseEditController extends BaseController{
    @FXML public TextField nameField;
    @FXML public Label statusLabel;

    private Use selectedUse;
    private UseService useService=UseService.getInstance();
    private final ContextMenu suggestions= new ContextMenu();
    private List<Use>allUses;

    @FXML
    public void initialize(){
        allUses=useService.getAllUses();

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

        allUses
                .stream()
                .filter(u->u.getName().startsWith(val))
                .limit(5)
                .forEach(u->{
                    MenuItem item = new MenuItem(u.getName());
                    item.setOnAction(e->{
                        nameField.setText(u.getName());
                        selectedUse=u;
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
            showAlert(Alert.AlertType.ERROR, "Invalid operation", "One must be logged in to edit");
            return;
        }
        
        validateUserInput();

        if (selectedUse == null) {
            insertNewUse();
        }
        else{
            updateExistingUse();
        }
    }

    private void updateExistingUse() {
        selectedUse.setName(nameField.getText());
        useService.save(selectedUse);
        log("UPDATE", "Use", nameField.getText());
        statusLabel.setText("Use successfully updated");
        statusLabel.setTextFill(Color.GREEN);
    }

    private void insertNewUse() {
        useService.save(new Use(nameField.getText()));
        log("INSERT", "Use", nameField.getText());
        statusLabel.setText("Use successfully inserted");
        statusLabel.setTextFill(Color.GREEN);
    }

    private void validateUserInput() {
        if(nameField.getText().isEmpty()){
            showAlert(Alert.AlertType.WARNING, "Validation", "Name cannot be empty");
            return;
        }
    }

    @Override
    protected Node getMainNode() {
        return nameField;
    }
}
