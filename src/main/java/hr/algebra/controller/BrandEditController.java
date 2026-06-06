package hr.algebra.controller;
import hr.algebra.exceptions.CannotPerformActionException;
import hr.algebra.models.Brand;
import hr.algebra.models.Session;
import hr.algebra.services.BrandService;
import hr.algebra.util.AlertUtil;
import hr.algebra.util.LoggingUtil;
import hr.algebra.util.SceneLoadingUtil;
import hr.algebra.util.XMLLoggingUtil;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import java.util.List;

public class BrandEditController extends BaseController
{

    @FXML private TextField nameField;
    @FXML private TextField descField;

    @FXML private Label statusLabel;

    private Brand selectedBrand; // za update
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
                        selectedBrand=b;
                        fillBrandDetails(b);
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

    private void fillBrandDetails(Brand b) {
        nameField.setText(b.getName());
        descField.setText(b.getDescription());
    }

    @FXML
    public void onBack(){
        SceneLoadingUtil.loadNewWindow("/hr/algebra/views/brand-view.fxml", "Brands", getMainNode());
    }

    @FXML
    public void onSave(){
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
            LoggingUtil.logInfo("UPDATE", "Brand", selectedBrand.getName(), Session.checkCurrentUserName());
            XMLLoggingUtil.log("INFO", "UPDATE", "successfuly deleted", "brand", Session.getCurrentUser().getUsername(), selectedBrand.getName());
            fillStatusLabel(statusLabel, "Brand successfully updated", Color.BLUE);
        }
        catch (CannotPerformActionException e){
            AlertUtil.showErrorAlert(e.getMessage());
        }

    }

    private void insertNewBrand() {
        try{
            brandService.save(new Brand(nameField.getText(), descField.getText()));
            LoggingUtil.logInfo("INSERT", "Brand", nameField.getText(), Session.checkCurrentUserName());
            XMLLoggingUtil.log("INFO", "INSERT", "successfully inserted", "brand", Session.checkCurrentUserName(),  nameField.getText());
            fillStatusLabel(statusLabel, "Brand successfully inserted", Color.GREEN);
        }
        catch (CannotPerformActionException e){
            AlertUtil.showErrorAlert(e.getMessage());
        }
    }

    private boolean validateUserInput() {
        return !nameField.getText().isEmpty();
    }

}


