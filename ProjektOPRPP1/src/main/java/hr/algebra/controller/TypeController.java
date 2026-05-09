package hr.algebra.controller;
import hr.algebra.models.Type;
import hr.algebra.services.TypeService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

public class TypeController extends BaseController{
    private final TypeService typeService=TypeService.getInstance();
    private Type selectedType;

    @FXML TableView<Type>typeTable;
    @FXML TableColumn<Type, Number> colID;
    @FXML TableColumn<Type, String> colName;

    @FXML TextField searchField;

    @FXML TextField nameField;

    @FXML Label currentUserLabel;
    @FXML
    public void initialize(){
        colID.setCellValueFactory(
                d->new SimpleIntegerProperty(d.getValue().getId()));
        colName.setCellValueFactory(
                d->new SimpleStringProperty(d.getValue().getName()));


        typeTable.getSelectionModel().selectedItemProperty()
                .addListener((obs,old,val)->{
                    if(val!=null){
                        selectedType=val;
                        nameField.setText(val.getName());
                    }
                });
        searchField.textProperty().addListener((obs,old,q)->{
            typeTable.setItems(FXCollections.observableArrayList(typeService.search(q)));
        });
        refresh();
    }

    @FXML
    public void onClear(){
        nameField.clear();
        typeTable.getSelectionModel().clearSelection();
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
        }
        else{ //postoji type s tim imenom pa ga updateaj
            selectedType.setName(nameField.getText());
            typeService.save(selectedType);
            log("UPDATE", "Type", selectedType.getName());
        }

        showAlert(Alert.AlertType.INFORMATION, "Success", "Type has been successfully saved");
        onClear();
        refresh();
    }

    @FXML
    public void onDelete(){
        if(currentUser==null){
            showAlert(Alert.AlertType.ERROR, "Error", "One must be logged in to edit");
            return;
        }
        if(nameField.getText().isEmpty()){
            showAlert(Alert.AlertType.WARNING, "Validation", "Name cannot be empty");
            return;
        }

        showConfirm("Confirm", "Do you want to delete: "+ nameField.getText());

        typeService.delete(selectedType.getId());
        log( "DELETE", "Type", selectedType.getName());
        showAlert(Alert.AlertType.INFORMATION, "Success", "Type successfully deleted");
        onClear();
        refresh();
    }


    @Override
    protected Node getMainNode() {
        return searchField;
    }

    private void refresh() {
        typeTable.setItems(FXCollections.observableArrayList(typeService.getAllTypes()));
    }

}
