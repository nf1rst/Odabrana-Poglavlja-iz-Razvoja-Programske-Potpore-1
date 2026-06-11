package hr.algebra.controller;

import hr.algebra.exceptions.CannotPerformActionException;
import hr.algebra.models.Session;
import hr.algebra.models.Use;
import hr.algebra.services.UseService;
import hr.algebra.util.AlertUtil;
import hr.algebra.util.LoggingUtil;
import hr.algebra.util.SceneLoadingUtil;
import hr.algebra.util.XMLLoggingUtil;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class UseController extends BaseController{

    private final UseService useService = UseService.getInstance();
    private Use selectedUse;

    @FXML private TextField searchField;

    @FXML private TableView<Use> useTable;
    @FXML private TableColumn<Use, Number>colID;
    @FXML private TableColumn<Use, String>colName;

    @FXML private Label nameLabel;

    @FXML private Label currentUserLabel;

    private ObservableList<Use> masterList;
    @FXML
    public void initialize(){
        masterList = FXCollections.observableArrayList(useService.getAllUses());
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

        fillUserLabel(currentUserLabel);
        useTable.setItems(masterList);

        searchField.textProperty().addListener((obs,old,q)->fillTable(useTable, useService.search(q)));

    }


    @FXML
    public void onClear(){
        nameLabel.setText("Name*");
        useTable.getSelectionModel().clearSelection();
        selectedUse=null;
    }

    @FXML
    public void onEdit(){
        if(!Session.userLoggedIn()){
            AlertUtil.showErrorAlert("One must be loggedin to edit");
            return;
        }
        SceneLoadingUtil.loadNewCustomWindow("/hr/algebra/views/insert-update-use-view.fxml", "Insert or update use", getMainNode(), 350,300);
    }


    @FXML
    public void onDelete(){
        if(currentUser ==null){
            AlertUtil.showErrorAlert( "One must be logged in to edit");
            return;
        }
        if(selectedUse==null){
            AlertUtil.showErrorAlert( "One must choose a use to delete");
            return;
        }

        if(!AlertUtil.showConfirmationAlert( "Do you want to delete: "+ selectedUse.getName()))
        {
            return;
        }

        try{
            useService.delete(selectedUse.getId());
            masterList.setAll(useService.getAllUses());
            LoggingUtil.logInfo( "DELETE", "Use", selectedUse.getName(), Session.checkCurrentUserName());
            XMLLoggingUtil.log("INFO", "DELETE", "successfully deleted", "use", Session.checkCurrentUserName(),  selectedUse.getName());
            selectedUse=null;
        }
        catch (CannotPerformActionException _){
            AlertUtil.showErrorAlert("Cannot delete use as it is associated with certain headphones");
        }
        useTable.setItems(masterList);
    }

    @Override
    protected Node getMainNode() {
        return searchField; // bilo sta samo da je u view
    }
}
