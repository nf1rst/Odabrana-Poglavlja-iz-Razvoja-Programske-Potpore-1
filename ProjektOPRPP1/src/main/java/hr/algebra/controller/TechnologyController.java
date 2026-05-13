package hr.algebra.controller;

import hr.algebra.models.Technology;
import hr.algebra.services.TechnologyService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;


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

        refresh();
    }

    @FXML
    public void onClear(){
        nameLabel.setText("Name*");
        descLabel.setText("Description*");
        technologiesTable.getSelectionModel().clearSelection();
    }

    @FXML
    public void onEdit(){
        openPopupWindow("/hr/algebra/views/insert-update-technology-view.fxml", "Insert and update technology", currentUser);
    }

    @FXML
    public void onDelete(){
        if(currentUser==null){
            showAlert(Alert.AlertType.ERROR, "Error" , "One must be logged in to edit");
            return;
        }
        if(selectedTechnology==null || selectedTechnology.getName().isEmpty()){

            showAlert(Alert.AlertType.WARNING, "Warning", "One must choose a technology to delete");
            return;
        }

        showConfirm("Confirmation", "Do you want to delete: "+ selectedTechnology.getName());

        technologyService.delete(selectedTechnology.getId());
        log( "DELETE", "Technology", selectedTechnology.getName());

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
