package hr.algebra.controller;

import hr.algebra.exceptions.CannotPerformActionException;
import hr.algebra.models.Session;
import hr.algebra.models.Technology;
import hr.algebra.services.TechnologyService;
import hr.algebra.util.AlertUtil;
import hr.algebra.util.LoggingUtil;
import hr.algebra.util.SceneLoadingUtil;
import hr.algebra.util.XMLLoggingUtil;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;


public class TechnologyController extends BaseController{
    private final TechnologyService technologyService= TechnologyService.getInstance();
    private Technology selectedTechnology;

    @FXML private TextField searchField;

    @FXML private TableView<Technology>technologiesTable;
    @FXML private TableColumn<Technology, Number> colID;
    @FXML private TableColumn<Technology, String> colName;
    @FXML private TableColumn<Technology, String> colDesc;

    @FXML private Label nameLabel;
    @FXML private Label descLabel;

    @FXML private Label currentUserLabel;

    private ObservableList<Technology> masterList;

    @FXML
    public void initialize(){
        colID.setCellValueFactory(
                d->new SimpleIntegerProperty(d.getValue().getId()));
        colName.setCellValueFactory(
                d->new SimpleStringProperty(d.getValue().getName()));
        colDesc.setCellValueFactory(
                d->new SimpleStringProperty(d.getValue().getDescription()));

        //ovo je da kad ti nesto odaberes u tablici da se on automatski napuni i na desno meniju
        technologiesTable.getSelectionModel().selectedItemProperty()
                .addListener((obs,old,val)->{
                    if(val!=null){
                        selectedTechnology=val;
                        nameLabel.setText(val.getName());
                        descLabel.setText(val.getDescription());
                    }
                });

        //za search field da filtrira kad korisnik nesto upise

        searchInit();

        fillUserLabel(currentUserLabel);

        masterList=FXCollections.observableArrayList(technologyService.getAllTechnologies());
        technologiesTable.setItems(masterList);
    }

    private void searchInit() {
        searchField.textProperty().addListener((obs, old, val)->{
            if(val!=null && !val.isEmpty()){
                observableListDataFill(technologiesTable, masterList, t->t.getName().contains(val));
            }
            else technologiesTable.setItems(masterList);
        });
    }

    @FXML
    public void onClear(){
        nameLabel.setText("Name*");
        descLabel.setText("Description*");
        technologiesTable.getSelectionModel().clearSelection();
        selectedTechnology=null;
    }

    @FXML
    public void onEdit(){
        if(!Session.userLoggedIn()){
            AlertUtil.showErrorAlert("One must be loggedin to edit");
            return;
        }
        SceneLoadingUtil.loadNewCustomWindow("/hr/algebra/views/insert-update-technology-view.fxml", "Insert and update technology", getMainNode(), 400, 350);
    }

    @FXML
    public void onDelete(){
        if(!Session.userLoggedIn()){
            AlertUtil.showErrorAlert( "One must be logged in to edit");
            return;
        }
        if(selectedTechnology==null){
            AlertUtil.showErrorAlert("One must choose a technology to delete");
            return;
        }

        if(!AlertUtil.showConfirmationAlert("Do you want to delete: "+ selectedTechnology.getName())){
            return ;
        }

        try{
            technologyService.delete(selectedTechnology.getId());
            LoggingUtil.logInfo( "DELETE", "Technology", selectedTechnology.getName(), Session.checkCurrentUserName());
            XMLLoggingUtil.log("INFO", "DELETE", "successfully deleted", "technology", Session.checkCurrentUserName(), selectedTechnology.getName());
        }
        catch (CannotPerformActionException _){
            AlertUtil.showErrorAlert("Cannot delete technology, as it is associated with one or more headphones");
        }

        selectedTechnology=null;
        onClear();
        fillTable(technologiesTable, technologyService.getAllTechnologies());
    }

    @Override
    protected Node getMainNode() {
        return searchField;
    }


}
