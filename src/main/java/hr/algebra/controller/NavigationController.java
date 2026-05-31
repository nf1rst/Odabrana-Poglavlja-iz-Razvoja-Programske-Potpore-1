package hr.algebra.controller;
import hr.algebra.models.Session;
import hr.algebra.util.AlertUtil;
import hr.algebra.util.SceneLoadingUtil;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class NavigationController extends BaseController{
    @FXML private HBox hBox;

    @FXML
    public void onMainMenu(){
        if(!Session.userLoggedIn()) SceneLoadingUtil.loadNewWindow("/hr/algebra/views/main-view-anonymus.fxml", "Main anonymous", getMainNode());
        else SceneLoadingUtil.loadNewWindow("/hr/algebra/views/main-view-loggedIn.fxml", "Main", getMainNode());
    }

    @FXML
    public void onHeadphones(){
        SceneLoadingUtil.loadNewWindow("/hr/algebra/views/headphones-view.fxml","Headphones" ,getMainNode());
    }

    @FXML
    public void onBrands(){
        SceneLoadingUtil.loadNewWindow("/hr/algebra/views/brand-view.fxml", "Brands", getMainNode());
    }

    @FXML
    public void onTypes(){
        SceneLoadingUtil.loadNewWindow("/hr/algebra/views/types-view.fxml", "Types", getMainNode());
    }

    @FXML
    public void onUses(){
        SceneLoadingUtil.loadNewWindow("/hr/algebra/views/uses-view.fxml", "Uses", getMainNode());
    }

    @FXML
    public void onTechnologies(){
        SceneLoadingUtil.loadNewWindow("/hr/algebra/views/technology-view.fxml", "Technologies", getMainNode());
    }

    @FXML
    public void onCompareHeadphones(){
        SceneLoadingUtil.loadNewWindow("/hr/algebra/views/compare-view.fxml", "Compare headphones", getMainNode());
    }

    @FXML
    public void onAccountInfo(){
        if(!Session.userLoggedIn()){
            AlertUtil.showErrorAlert( "To view account details, one must be logged in");
            return;
        }
        SceneLoadingUtil.loadNewWindow("/hr/algebra/views/account-details-view.fxml", "Account", getMainNode());
    }

    @FXML
    public void onLogOut(){
        Session.setCurrentUser(null);
        SceneLoadingUtil.loadNewCustomWindow("/hr/algebra/views/login-view.fxml", "Login",getMainNode(), 400,500);
    }

    @Override
    protected Node getMainNode() {
        return hBox; // menuItem se ovdje ne moze vratit jer ne nasljeduje klasu node
    }
}
