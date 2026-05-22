package hr.algebra.controller;
import hr.algebra.exceptions.CannotPerformActionException;
import hr.algebra.models.User;
import hr.algebra.services.AdminService;
import hr.algebra.services.UserService;
import hr.algebra.util.AlertUtil;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

public class AdminController extends BaseController{

    @FXML private Label adminLabel;
    @FXML private TextField searchField;
    @FXML private ChoiceBox<String>editRightsChoiceBox;

    @FXML private TableView<User> userTableView;
    @FXML private TableColumn<User, Number>colID;
    @FXML private TableColumn<User, String>colUsername;
    @FXML private TableColumn<User, String>colPasswordHash;

    @FXML private Label headphonesCount;
    @FXML private Label typesCount;
    @FXML private Label brandsCount;
    @FXML private Label usesCount;
    @FXML private Label techCount;
    @FXML private Label usersCount;

    private User selectedUser;

    private final UserService userService = UserService.getInstance();
    private AdminService adminService = AdminService.INSTANCE;

    @FXML
    public void initialize(){
        setUpColumns();

        searchField.textProperty().addListener((obs, old, val)->{
            if(!val.isBlank()){
                try{
                    userTableView.setItems(FXCollections.observableArrayList(userService.search(val)));
                }
                catch (CannotPerformActionException e){
                    logger.error("CannotPerformActionException", e);
                    AlertUtil.showErrorAlert("Cannot perform action at this time");
                }
            }
            else{
                try{
                    userTableView.setItems(FXCollections.observableArrayList(userService.getAllUsers()));
                }catch (CannotPerformActionException e){
                    logger.error("CannotPerformActionException", e);
                    AlertUtil.showErrorAlert("Cannot perform action at this time");
                }
            }
        });

        userTableView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, old, val)->{
                    if(val!=null){
                        selectedUser=val;
                        System.out.println(selectedUser.getUsername());
                    }
                });
        fillUserTable();
    }

    private void refresh() {
        try{
            userTableView.setItems(FXCollections.observableArrayList(userService.getAllUsers()));
        }
        catch (CannotPerformActionException e){
            logger.error("CannotPerformActionException", e);
            AlertUtil.showErrorAlert("Cannot perform this action at the moment");
        }
    }

    private void fillUserTable() {
        userTableView.setItems(FXCollections.observableArrayList(userService.getAllUsers()));
    }

    private void setUpColumns() {
        colID.setCellValueFactory
                (c->new SimpleIntegerProperty(c.getValue().getId()));

        colUsername.setCellValueFactory
                (c->new SimpleStringProperty(c.getValue().getUsername()));

        colPasswordHash.setCellValueFactory
                (c->new SimpleStringProperty(c.getValue().getPassword()));
    }

    @FXML
    public void onListAllUsers(){
        try{
            userTableView.setItems(FXCollections.observableArrayList(userService.getAllUsers()));
        }
        catch (CannotPerformActionException e){
            AlertUtil.showErrorAlert("Cannot perform this action at the moment");
            logger.error("CannotPerformActionException", e);
        }
        clear();
    }

    private void clear() {
        searchField.clear();
    }

    @FXML
    public void onSeeDBStats(){
        try{
            System.out.println("under construction");
        }
        catch (CannotPerformActionException _){
            AlertUtil.showErrorAlert("Cannot execute see database stats");
        }
    }

    @FXML
    public void onDeleteAllEntities(){
        try{
            adminService.deleteAllEntities();
        }
        catch (CannotPerformActionException e){
            AlertUtil.showErrorAlert("Cannot delete all database content");
            //ovdje ne treba logger jer je taj exception vec zapisan u service sloju u logove, ovo je samo da user zna sto se dogodilo
        }
    }

    @FXML
    public void onRestartDB(){
        try{
            adminService.deleteDatabase();
        }
        catch (CannotPerformActionException _s){
            AlertUtil.showErrorAlert("Cannot drop database");
        }
    }

    @FXML
    public void onDeleteUser(){
        try{
            if(selectedUser==null){
                AlertUtil.showErrorAlert("No user was selected for deletion");
                return;
            }
            adminService.deleteUser(currentUser.getUsername());
        }
        catch (CannotPerformActionException _){
            AlertUtil.showErrorAlert("Cannot delete user");
        }
    }

    @FXML
    public void onDeleteAllUsers(){
        try{
            adminService.deleteAllUsers();
        }
        catch (CannotPerformActionException _){
            AlertUtil.showErrorAlert("Cannot delete all users");
        }
    }


    @Override
    protected Node getMainNode() {
        return adminLabel;
    }
}
