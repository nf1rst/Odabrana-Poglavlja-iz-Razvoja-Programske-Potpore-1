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

    @FXML private TextField nameField;
    @FXML private TextField descField;

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
                        nameField.setText(val.getName());
                        descField.setText(val.getDescription()!=null?val.getDescription():"...");
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
        nameField.clear();
        descField.clear();
        technologiesTable.getSelectionModel().clearSelection();
    }

    @FXML
    public void onSave(){
        if(currentUser==null){
            showAlert(Alert.AlertType.ERROR, "Error", "One must be logged in to edit");
            return;
        }
        if(nameField.getText().isEmpty()){
            showAlert(Alert.AlertType.WARNING, "Warning", "Name must not be empty");
            return;
        }
        if(selectedTechnology==null){ //tehnologija ne postoji onda je mora ubacit
            technologyService.save(new Technology(nameField.getText(), descField.getText()));
            log("INSERT", "Technology", nameField.getText());
        }
        else{// ako vec postoji onda se treba napravit update te tehnologije s istim IDjem
            selectedTechnology.setName(nameField.getText());
            selectedTechnology.setDescription(descField.getText());
            technologyService.save(selectedTechnology);
            log("UPDATE", "Technology", nameField.getText());
        }

        showAlert(Alert.AlertType.INFORMATION, "Success", "Technology has been successfully saved");
        refresh();
    }

    @FXML
    public void onDelete(){
        if(currentUser==null){
            showAlert(Alert.AlertType.ERROR, "Error" , "One must be logged in to edit");
            return;
        }
        if(nameField.getText().isEmpty()){
            showAlert(Alert.AlertType.WARNING, "Warning", "One must choose a technology to delete");
            return;
        }

        showConfirm("Confirmation", "Do you want to delete: "+ nameField.getText());

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
        currentUserLabel.setText("User: " + (currentUser!=null?currentUser.getUsername():"Not loggen in"));
    }

}
