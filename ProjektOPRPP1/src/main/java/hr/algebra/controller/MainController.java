package hr.algebra.controller;
import hr.algebra.models.Session;
import hr.algebra.models.User;
import hr.algebra.util.AlertUtil;
import hr.algebra.util.SceneLoadingUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MainController {

    private final Logger logger = LoggerFactory.getLogger(MainController.class);

    @FXML private Label welcomeLabel;

    @FXML
    public void initialize(){
        if(!Session.userLoggedIn()) welcomeLabel.setText("Welcome anonymous");
        else welcomeLabel.setText("Welcome " + Session.getCurrentUser().getUsername());
    }

    private Node getMainNode(){
        return welcomeLabel;
    }

    @FXML public void onHeadphones(){
        SceneLoadingUtil.loadNewWindow("/hr/algebra/views/headphones-view.fxml", "Headphones Page", getMainNode());
    }
    @FXML public void onBrands(){
        SceneLoadingUtil.loadNewWindow("/hr/algebra/views/brand-view.fxml", "Brands Page", getMainNode());
    }
    @FXML public void onTechnologies(){
        SceneLoadingUtil.loadNewWindow("/hr/algebra/views/technology-view.fxml", "Technology page", welcomeLabel);
    }
    @FXML public void onCompareHeadphones(){SceneLoadingUtil.loadNewWindow("/hr/algebra/views/compare-view.fxml", "Compare Headphones page", getMainNode());}
    @FXML public void onTypes(){SceneLoadingUtil.loadNewWindow("/hr/algebra/views/types-view.fxml", "Type page", getMainNode());}
    @FXML public void onUses(){SceneLoadingUtil.loadNewWindow("/hr/algebra/views/uses-view.fxml", "Uses page", getMainNode());}
    @FXML public void onLogin() {SceneLoadingUtil.loadNewCustomWindow("/hr/algebra/views/login-view.fxml", "Login Page", getMainNode(), 400, 500);}
    @FXML public void onAccountDetails(){
        if(!Session.userLoggedIn()){
            AlertUtil.showErrorAlert("To view account details, one must be logged in");
            return;
        }
        SceneLoadingUtil.loadNewWindow("/hr/algebra/views/account-details-view.fxml", "Account info", getMainNode());
    }

    @FXML public void onLogOut(){
        hr.algebra.models.Session.setCurrentUser(null);
        SceneLoadingUtil.loadNewCustomWindow("/hr/algebra/views/login-view.fxml", "Login Page", getMainNode(),400,500);
    }

}
