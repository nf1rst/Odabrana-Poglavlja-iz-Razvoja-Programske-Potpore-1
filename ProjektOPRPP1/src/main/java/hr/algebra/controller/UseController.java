package hr.algebra.controller;
import hr.algebra.models.Use;
import hr.algebra.services.UseService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

public class UseController extends BaseController{
    private final UseService useService = UseService.getInstance();
    private Use selectedUse;

    @FXML private TextField searchField;

    @FXML private TableView<Use> useTable;
    @FXML private TableColumn<Use, Number>colID;
    @FXML private TableColumn<Use, String>colName;

    @FXML private Label nameLabel;

    @FXML
    public void initialize(){
        colID.setCellValueFactory(
                d->new SimpleIntegerProperty(d.getValue().getId()));
        colName.setCellValueFactory(
                d->new SimpleStringProperty(d.getValue().getName()));

        useTable.getSelectionModel().selectedItemProperty()
                .addListener((obs,old,val)->{
                    if(val!=null){
                        selectedUse=val;
                        nameLabel.setText(val.getName());
                    }
                });

 refresh();

        searchField.textProperty().addListener((obs,old,q)->useTable.setItems(FXCollections.observableArrayList(useService.search(q))));
    
    }

    @FXML
    public void onClear(){
        nameLabel.setText("Name*");
        useTable.getSelectionModel().clearSelection();
    }

    @FXML
    public void onEdit(){
        openPopupWindow("/hr/algebra/views/insert-update-use-view.fxml", "Insert or update use", currentUser);
    }


    @FXML
    public void onDelete(){
        if(currentUser ==null){
            showAlert(Alert.AlertType.ERROR, "Invalid operation", "One must be logged in to edit");
            return;
        }
        if(selectedUse==null ||  selectedUse.getName().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Error", "Name cannot be empty");
            return;
        }

        showConfirm("Confirmation", "Do you want to delete: "+ selectedUse.getName());

        useService.delete(selectedUse.getId());
        log( "DELETE", "Use", selectedUse.getName());
        refresh();
    }

    private void refresh() {
        useTable.setItems(FXCollections.observableArrayList(useService.getAllUses()));
    }

    @Override
    protected Node getMainNode() {
        return searchField; // bilo sta samo da je u view
    }
}
