package hr.algebra.controller;

import hr.algebra.models.Brand;
import hr.algebra.services.BrandService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;


public class BrandController extends BaseController {
    @FXML private TextField searchField;

    @FXML private TableView<Brand> brandTable;
    @FXML private TableColumn<Brand,Number>colId;
    @FXML private TableColumn<Brand, String> colName;
    @FXML private TableColumn<Brand, String> colDesc;

    @FXML private Label nameLabel;
    @FXML private ImageView brandImageView;
    @FXML private Label descLabel;


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
                nameLabel.setText(val.getName()!=null?val.getName():"N/A");
                descLabel.setText(val.getDescription()!=null?val.getDescription():"N/A");
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
        nameLabel.setText("Name*");
        descLabel.setText("Description*");
        brandTable.getSelectionModel().clearSelection();
    }

    @FXML
    public void onOpenSave(){
        openPopupWindow("/hr/algebra/views/insert-update-brand-view.fxml", "Insert or Update Brand", currentUser);
    }

    @FXML
    public void onDelete(){
        if(currentUser==null){
            showAlert(Alert.AlertType.ERROR, "Error", "To edit brands you must be logged in");
            return;
        }

        if(selectedBrand==null ||selectedBrand.getName().isEmpty()){ // prvo provjeri jel null jer uopce ne moze do get name ako je
            showAlert(Alert.AlertType.WARNING, "Warning", "One must choose a brand to delete");
            return;
        }

        if(!showConfirm("Confirmation", "Do you want to delete: " + selectedBrand.getName()))return;

        brandService.delete(selectedBrand.getId());
        log("DELETE", "Brand", selectedBrand.getName());
        showAlert(Alert.AlertType.INFORMATION, "Success", "Brand successfully deleted");
        onNew();
        refresh();
    }


    private void refresh() {
        brandTable.setItems(FXCollections.observableArrayList(brandService.getAllBrands()));
    }


    @Override
    protected Node getMainNode() {
        return searchField;
    }
}
