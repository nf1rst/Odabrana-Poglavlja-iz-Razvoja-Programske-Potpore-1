package hr.algebra.controller;
import hr.algebra.models.Session;
import hr.algebra.models.Use;
import hr.algebra.services.UseService;
import hr.algebra.util.AlertUtil;
import hr.algebra.util.LoggingUtil;
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
                        selectedUse=u;
                        nameField.setText(u.getName());
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
            AlertUtil.showErrorAlert( "Admin does not allow editing at the moment ");
            return;
        }
        
       if(!validateUserInput()){
           return;
       }

        trySaveUse();
        selectedUse=null;
    }

    private void trySaveUse() {
        if (!useService.nameExists(nameField.getText())) {
            insertNewUse();
        }
        else{
            updateExistingUse();
        }
    }

    private void updateExistingUse() {
        selectedUse.setName(nameField.getText());
        useService.save(selectedUse);
        LoggingUtil.logInfo("UPDATE", "Use", nameField.getText(), Session.getCurrentUser().getUsername());
        fillStatusLabel(statusLabel,"Use successfully updated", Color.BLUE);

    }


    private void insertNewUse() {
        useService.save(new Use(nameField.getText()));
        LoggingUtil.logInfo("INSERT", "Use", nameField.getText(), Session.getCurrentUser().getUsername());
        fillStatusLabel(statusLabel,"Use successfully inserted", Color.GREEN);
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
