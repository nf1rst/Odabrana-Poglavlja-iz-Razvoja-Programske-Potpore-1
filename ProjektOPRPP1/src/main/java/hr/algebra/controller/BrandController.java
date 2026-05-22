package hr.algebra.controller;

import hr.algebra.exceptions.CannotPerformActionException;
import hr.algebra.models.Brand;
import hr.algebra.models.Session;
import hr.algebra.models.User;
import hr.algebra.services.BrandService;
import hr.algebra.util.AlertUtil;
import hr.algebra.util.SceneLoadingUtil;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;


public class BrandController extends BaseController {

    @FXML private TextField searchField;

    @FXML private NavigationController  navigationController;

    @FXML private TableView<Brand> brandTable;
    @FXML private TableColumn<Brand,Number>colId;
    @FXML private TableColumn<Brand, String> colName;
    @FXML private TableColumn<Brand, String> colDesc;

    @FXML private Label nameLabel;
    @FXML private ImageView brandImageView;
    @FXML private Label descLabel;

    @FXML private Label currentUserLabel;

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

        //refresh();

        brandTable.getSelectionModel().selectedItemProperty()
                .addListener((obs,old,val)->{
            if(val!=null){
                selectedBrand=val;
                nameLabel.setText(val.getName()!=null?val.getName():"N/A");
                descLabel.setText(val.getDescription()!=null?val.getDescription():"N/A");
            }
        });

        searchField.textProperty().addListener((obs,old,q)->{
            brandTable.setItems(FXCollections.observableArrayList(brandService.search(q)));
        });

        fillUserLabel(currentUserLabel);
    }



    @FXML
    public void onNew(){
        nameLabel.setText("Name*");
        descLabel.setText("Description*");
        brandTable.getSelectionModel().clearSelection();
    }

    @FXML
    public void onOpenSave(){
        if(!Session.userCanEdit()){
            AlertUtil.showErrorAlert("One must be loggedd in to edit");
            return;
        }
        SceneLoadingUtil.loadPopUpWindow("/hr/algebra/views/insert-update-brand-view.fxml", "Insert or Update Brand");
    }

    @FXML
    public void onDelete(){
        if(!Session.userLoggedIn()){
            AlertUtil.showErrorAlert("To edit brands you must be logged in");
            return;
        }

        if(selectedBrand==null ||selectedBrand.getName().isEmpty()){ // prvo provjeri jel null jer uopce ne moze do get name ako je
            AlertUtil.showErrorAlert( "One must choose a brand to delete");
            return;
        }

        if(!AlertUtil.showConfirmationAlert("Do you want to delete: " + selectedBrand.getName())){
            return;
        }

        try{
            brandService.delete(selectedBrand.getId());
            logger.error("DELETE", "Brand", selectedBrand.getName());
        }
        catch (CannotPerformActionException e){
            AlertUtil.showErrorAlert(e.getMessage());
        }
        onNew();
        refresh();
    }


    private void refresh() {
        brandTable.setItems(FXCollections.observableArrayList(brandService.getAllBrands()));
    }


    @Override
    protected Node getMainNode() {
        return brandTable;
    }
}
