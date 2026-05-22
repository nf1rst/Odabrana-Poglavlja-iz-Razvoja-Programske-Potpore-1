package hr.algebra.controller;
import hr.algebra.models.Session;
import hr.algebra.models.Use;
import hr.algebra.services.UseService;
import hr.algebra.util.AlertUtil;
import hr.algebra.util.SceneLoadingUtil;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

public class UseController extends BaseController{

    @FXML private NavigationController  navigationController;

    private final UseService useService = UseService.getInstance();
    private Use selectedUse;

    @FXML private TextField searchField;

    @FXML private TableView<Use> useTable;
    @FXML private TableColumn<Use, Number>colID;
    @FXML private TableColumn<Use, String>colName;

    @FXML private Label nameLabel;

    @FXML private Label currentUserLabel;

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

        //refresh();

        fillUserLabel(currentUserLabel);

        searchField.textProperty().addListener((obs,old,q)->useTable.setItems(FXCollections.observableArrayList(useService.search(q))));
    
    }

    @FXML
    public void onClear(){
        nameLabel.setText("Name*");
        useTable.getSelectionModel().clearSelection();
    }

    @FXML
    public void onEdit(){
        if(!Session.userCanEdit()){
            AlertUtil.showErrorAlert("One must be loggedin to edit");
            return;
        }
        SceneLoadingUtil.loadPopUpWindow("/hr/algebra/views/insert-update-use-view.fxml", "Insert or update use");
    }


    @FXML
    public void onDelete(){
        if(currentUser ==null){
            AlertUtil.showErrorAlert( "One must be logged in to edit");
            return;
        }
        if(selectedUse==null ||  selectedUse.getName().isEmpty()){
            AlertUtil.showErrorAlert( "Name cannot be empty");
            return;
        }

        if(!AlertUtil.showConfirmationAlert( "Do you want to delete: "+ selectedUse.getName()))
        {
            return;
        }

        useService.delete(selectedUse.getId());
        logger.info( "DELETE", "Use", selectedUse.getName());
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
