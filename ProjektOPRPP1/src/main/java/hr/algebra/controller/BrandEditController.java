package hr.algebra.controller;
import hr.algebra.models.Brand;
import hr.algebra.services.BrandService;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import java.util.List;

public class BrandEditController extends BaseController
{
    @FXML private TextField nameField; // ovo tu ce trebat kad ce doc popup
    @FXML private TextField descField;

    private Brand selectedBrand;
    private BrandService brandService=BrandService.getInstance();
    private final ContextMenu suggestions = new ContextMenu();
    private List<Brand> allBrands;

    @Override
    protected Node getMainNode() {
        return nameField;
    }

    @FXML
    public void initialize() {

        allBrands = brandService.getAllBrands();

        nameField.textProperty().addListener((obs, old, val) -> {

            if (val == null || val.isBlank()) {
                suggestions.hide();
                return;
            }

            showSuggestions(val);
        });
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
        //onNew();//da makne sve selektirano
        //refresh(); // da prikazu dobro ono sto se primjernilo
    }

}


