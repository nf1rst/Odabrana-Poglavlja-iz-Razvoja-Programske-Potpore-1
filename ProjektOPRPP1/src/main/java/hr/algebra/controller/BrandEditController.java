package hr.algebra.controller;
import hr.algebra.exceptions.CannotPerformActionException;
import hr.algebra.models.Brand;
import hr.algebra.models.Session;
import hr.algebra.services.BrandService;
import hr.algebra.util.AlertUtil;
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
        if(!Session.userCanEdit()){
            AlertUtil.showErrorAlert("To edit brands you must be logged in");
            return;
        }

        validateUserInput();

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
            logger.info( "UPDATE", "Brand", nameField.getText());
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
            logger.info("INSERT", "Brand", nameField.getText());
            statusLabel.setText("Brand successfully inserted");
            statusLabel.setTextFill(Color.GREEN);
        }
        catch (CannotPerformActionException e){
            AlertUtil.showErrorAlert(e.getMessage());
        }
    }

    private void validateUserInput() {
        if(nameField.getText().isEmpty()){
            AlertUtil.showErrorAlert("Name cannot be empty");
        }
    }

}


