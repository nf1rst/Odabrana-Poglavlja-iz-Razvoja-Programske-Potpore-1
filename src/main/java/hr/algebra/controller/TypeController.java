package hr.algebra.controller;
import hr.algebra.exceptions.CannotPerformActionException;
import hr.algebra.models.Session;
import hr.algebra.models.Type;
import hr.algebra.services.TypeService;
import hr.algebra.util.AlertUtil;
import hr.algebra.util.LoggingUtil;
import hr.algebra.util.SceneLoadingUtil;
import hr.algebra.util.XMLLoggingUtil;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
    @FXML Label descLabel;

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
        searchField.textProperty().addListener((obs,old,q)->fillTable(typeTable, typeService.search(q)));

        fillUserLabel(currentUserLabel);
        fillTable(typeTable, typeService.getAllTypes());
    }

    @FXML
    public void onClear(){
        nameLabel.setText("Name*");
        typeTable.getSelectionModel().clearSelection();
        selectedType=null;
    }


    @FXML
    public void onEdit(){
        if(!Session.userLoggedIn()){
            AlertUtil.showErrorAlert("One must be loggedin to edit");
            return;
        }
        SceneLoadingUtil.loadNewCustomWindow("/hr/algebra/views/insert-update-type-view.fxml", "Insert and Update Type", getMainNode(), 350,400);
    }

    @FXML
    public void onDelete(){
        if(currentUser==null){
            AlertUtil.showErrorAlert("One must be logged in to edit");
            return;
        }
        if(selectedType==null){
            AlertUtil.showErrorAlert( "One must choose a type to delete");
            return;
        }

        if(!AlertUtil.showConfirmationAlert( "Do you want to delete: "+ selectedType.getName())){
            return;
        }

        try{
            typeService.delete(selectedType.getId());
            LoggingUtil.logInfo( "DELETE", "Type", selectedType.getName(), Session.checkCurrentUserName());
            XMLLoggingUtil.log("INFO", "DELETE", "successfully deleted", "type", Session.checkCurrentUserName(),  nameLabel.getText());
            selectedType=null;
            onClear();
        }
        catch (CannotPerformActionException _){
            AlertUtil.showErrorAlert("Cannot delete type as it is associated with certain headphones");
        }
        fillTable(typeTable, typeService.getAllTypes());
    }


    @Override
    protected Node getMainNode() {
        return searchField;
    }


}
