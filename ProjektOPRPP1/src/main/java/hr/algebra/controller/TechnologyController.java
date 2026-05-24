package hr.algebra.controller;

import hr.algebra.models.Session;
import hr.algebra.models.Technology;
import hr.algebra.services.TechnologyService;
import hr.algebra.util.AlertUtil;
import hr.algebra.util.LoggingUtil;
import hr.algebra.util.SceneLoadingUtil;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;


public class TechnologyController extends BaseController{

    @FXML private NavigationController  navigationController;

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
        searchField.textProperty().addListener((obs,old,q)->{
            technologiesTable.setItems(FXCollections.observableArrayList(technologyService.search(q)));
        });

        fillUserLabel(currentUserLabel);

        refresh();
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
        SceneLoadingUtil.loadPopUpWindow("/hr/algebra/views/insert-update-technology-view.fxml", "Insert and update technology");
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

        technologyService.delete(selectedTechnology.getId());

        LoggingUtil.logInfo( "DELETE", "Technology", selectedTechnology.getName(), Session.getCurrentUser().getUsername());

        selectedTechnology=null;
        onClear();
        refresh();
    }

    @Override
    protected Node getMainNode() {
        return searchField;
    }

    private void refresh() {
        technologiesTable.setItems(FXCollections.observableArrayList(technologyService.getAllTechnologies()));
    }

}
