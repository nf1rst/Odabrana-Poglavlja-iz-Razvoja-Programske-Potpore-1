package hr.algebra.controller;
import hr.algebra.exceptions.CannotPerformActionException;
import hr.algebra.models.Brand;
import hr.algebra.models.Session;
import hr.algebra.services.BrandService;
import hr.algebra.util.AlertUtil;
import hr.algebra.util.LoggingUtil;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import java.util.List;

public class BrandEditController extends BaseController
{
    @FXML private TextField nameField; // ovo tu ce trebat kad ce doc popup
    @FXML private TextField descField;

    @FXML private Label statusLabel;

    private Brand selectedBrand;
    private final BrandService brandService=BrandService.getInstance();
    private final ContextMenu suggestions = new ContextMenu();
    private List<Brand> allBrands;

    @Override
    protected Node getMainNode() {
        return nameField;
    }

    @FXML
    public void initialize() {

        allBrands = fillList();

        nameField.textProperty().addListener((obs, old, val) -> {

            if (val == null || val.isBlank()) {
                suggestions.hide();
                return;
            }
            showSuggestions(val);
        });
    }

    private List<Brand> fillList() {
        try{
            allBrands=brandService.getAllBrands();
        }
        catch (CannotPerformActionException e){
            AlertUtil.showErrorAlert(e.getMessage());
        }
        return allBrands;
    }

    private void showSuggestions(String val) {
        suggestions.getItems().clear();

        allBrands
                .stream()
                .filter(b->b.getName().startsWith(val))
                .limit(5)
                .forEach(b ->{
                    MenuItem item = new MenuItem(b.getName());
                    item.setOnAction(e->{
                        nameField.setText(b.getName());
                        selectedBrand=b;
                        descField.setText(b.getDescription());
                        suggestions.hide();
                    });
                    suggestions.getItems().add(item);
                });
        if(!suggestions.getItems().isEmpty()){
            suggestions.show(nameField, Side.BOTTOM, 0,0);
        }
        else{
            suggestions.hide();
        }
    }

    @FXML
    public void onSave(){
        System.out.println(Session.userCanEdit());
        if(!Session.userCanEdit()){
            AlertUtil.showErrorAlert("Admin does not allow editing at the moment ");
            return;
        }


        if(!validateUserInput()){
            AlertUtil.showErrorAlert("Name must not be left blank");
            return;
        }

        if(!brandService.nameExists(nameField.getText())){
            insertNewBrand();
        }
        else{
            updateExistingBrand();
        }
    }

    private void updateExistingBrand() {
        selectedBrand.setName(nameField.getText());
        selectedBrand.setDescription(descField.getText());

        try{
            brandService.save(selectedBrand);
            LoggingUtil.logInfo("DELETE", "Brand", selectedBrand.getName(), Session.getCurrentUser().getUsername());
            statusLabel.setText("Brand successfully updated");
            statusLabel.setTextFill(Color.GREEN);
        }
        catch (CannotPerformActionException e){
            AlertUtil.showErrorAlert(e.getMessage());
        }

    }

    private void insertNewBrand() {
        try{
            brandService.save(new Brand(nameField.getText(), descField.getText()));
            LoggingUtil.logInfo("INSERT", "Brand", nameField.getText(), Session.getCurrentUser().getUsername());
            statusLabel.setText("Brand successfully inserted");
            statusLabel.setTextFill(Color.GREEN);
        }
        catch (CannotPerformActionException e){
            AlertUtil.showErrorAlert(e.getMessage());
        }
    }

    private boolean validateUserInput() {
        return !nameField.getText().isEmpty();
    }

}


