package hr.algebra.controller;

import hr.algebra.models.Brand;
import hr.algebra.models.User;
import hr.algebra.services.BrandService;
import hr.algebra.xml.ActionLogger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;


public class BrandController extends BaseController {
    @FXML private TextField searchField;

    @FXML private TableView<Brand> brandTable;
    @FXML private TableColumn<Brand,Number>colId;
    @FXML private TableColumn<Brand, String> colName;
    @FXML private TableColumn<Brand, String> colDesc;

    @FXML private TextField nameField;
    @FXML private TextField descField;

    private final BrandService brandService = BrandService.getInstance();
    private Brand selectedBrand=null;

    @FXML
    public void initialize(){
        colId.setCellValueFactory(
                d-> new SimpleIntegerProperty(d.getValue().getId()));
        colName.setCellValueFactory(
                d->new SimpleStringProperty(d.getValue().getName()));
        colDesc.setCellValueFactory(
                d->new SimpleStringProperty(d.getValue().getDescription()));

        refresh();

        //kako ovo tocno radi
        brandTable.getSelectionModel().selectedItemProperty()
                .addListener((obs,old,val)->{
            if(val!=null){
                selectedBrand=val;
                nameField.setText(val.getName());
                descField.setText(val.getDescription()!=null?val.getDescription():"...");
            }
        });

        //kako ovo tocno radi
        searchField.textProperty().addListener((obs,old,q)->{
            brandTable.setItems(FXCollections.observableArrayList(brandService.search(q)));
        });

        //refresh(); //da se odmah ucitaju svi brendovi, ne da stoji prazno
    }

    @FXML
    public void onNew(){
        nameField.clear();
        descField.clear();
        brandTable.getSelectionModel().clearSelection();
    }

    @FXML
    public void onDelete(){
        if(currentUser==null){
            showAlert(Alert.AlertType.ERROR, "Error", "To edit brands you must be logged in");
            return;
        }

        if(nameField.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "One must choose a brand to delete");
            return;
        }

        if(!showConfirm("Confirmation", "Do you want to delete: " + nameField.getText()))return;

        brandService.delete(selectedBrand.getId());
        log("DELETE", "Brand", selectedBrand.getName());
        showAlert(Alert.AlertType.INFORMATION, "Success", "Brand successfully deleted");
        onNew();
        refresh();
    }


    //ideja: korisnik u meni desno upisuje brand i opis, ako on postoji onda ce ga selektirat i updateat, ako on ne postoji onda ce se taj brend ubacit u bazu
    //jel dobra ideja ubacit neki warning tipa: BRAND DOESN'T EXIST, DO YOU WANT TO ADD IT?
    @FXML
    public void onSave(){
        if(currentUser==null){
            showAlert(Alert.AlertType.ERROR, "Error", "To edit brands you must be logged in");
            return;
        }

        String name = nameField.getText();
        if(name.isEmpty()){
            showAlert(Alert.AlertType.WARNING, "Validation", "Name cannot be empty");
            return;
        }
        if(selectedBrand==null){
            brandService.save(new Brand(name, descField.getText()));
            log("INSERT", "Brand", name);
        }
        else{
            selectedBrand.setName(name);
            selectedBrand.setDescription(descField.getText());
            brandService.save(selectedBrand);
            log( "UPDATE", "Brand", name);
        }
        showAlert(Alert.AlertType.INFORMATION, "Success", "Brand has been successfully saved");
        onNew();//da makne sve selektirano
        refresh(); // da prikazu dobro ono sto se primjernilo
    }


    private void refresh() {
        brandTable.setItems(FXCollections.observableArrayList(brandService.getAllBrands()));
        //currentUserLabel.setText("User: " + (currentUser!=null?currentUser.getUsername():"Not logged in "));
        //ako ovdje nema zagrada nakon plusa se sve raspadne jer nikad nece doc null vrijednost za provjeru ( jer se zbraja string "user" i potencialno null)  ali moze doci za ispis nakon upitnika
    }


    @Override
    protected Node getMainNode() {
        return searchField;
    }
}
