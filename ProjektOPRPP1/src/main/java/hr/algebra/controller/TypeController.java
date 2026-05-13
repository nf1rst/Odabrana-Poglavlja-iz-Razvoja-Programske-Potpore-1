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

    @FXML Label nameLabel;

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
                        nameLabel.setText(val.getName());
                    }
                });
        searchField.textProperty().addListener((obs,old,q)->{
            typeTable.setItems(FXCollections.observableArrayList(typeService.search(q)));
        });
        refresh();
    }

    @FXML
    public void onClear(){
        nameLabel.setText("Name*");
        typeTable.getSelectionModel().clearSelection();
    }


    @FXML
    public void onEdit(){
        openPopupWindow("/hr/algebra/views/insert-update-type-view.fxml", "Insert and Update Type", currentUser);
    }

    @FXML
    public void onDelete(){
        if(currentUser==null){
            showAlert(Alert.AlertType.ERROR, "Error", "One must be logged in to edit");
            return;
        }
        if(selectedType.getName().isEmpty()){
            showAlert(Alert.AlertType.WARNING, "Validation", "Name cannot be empty");
            return;
        }

        showConfirm("Confirm", "Do you want to delete: "+ selectedType.getName());

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
