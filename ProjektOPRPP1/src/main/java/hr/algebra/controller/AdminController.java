package hr.algebra.controller;
import hr.algebra.exceptions.CannotPerformActionException;
import hr.algebra.models.DBStatistics;
import hr.algebra.models.Session;
import hr.algebra.models.User;
import hr.algebra.services.AdminService;
import hr.algebra.services.HeadphonesService;
import hr.algebra.services.UserService;
import hr.algebra.util.AlertUtil;
import hr.algebra.util.LoggingUtil;
import hr.algebra.xml.export.XMLExporter;
import jakarta.xml.bind.JAXBException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.util.Optional;

public class AdminController extends BaseController{

    @FXML private Label adminLabel;
    @FXML private TextField searchField;
    @FXML private CheckBox editRightsCheckBox;

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
    private final HeadphonesService headphonesService = HeadphonesService.getInstance();
    private static final AdminService adminService = AdminService.INSTANCE;

    @FXML
    public void initialize(){
        setUpColumns();
        setUpChoiceBox();

        searchField.textProperty().addListener((obs, old, val)->{
            if(!val.isBlank()){
                try{
                    userTableView.setItems(FXCollections.observableArrayList(userService.search(val)));
                }
                catch (CannotPerformActionException e){
                    LoggingUtil.logError("search error", e);
                    AlertUtil.showErrorAlert("Cannot perform action at this time");
                }
            }
            else{
                try{
                    userTableView.setItems(FXCollections.observableArrayList(userService.getAllUsers()));
                }catch (CannotPerformActionException e){
                    LoggingUtil.logError("get all users error", e);
                    AlertUtil.showErrorAlert("Cannot perform action at this time");
                }
            }
        });

        userTableView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, old, val)->{
                    if(val!=null){
                        selectedUser=val;
                    }
                });
        fillUserTable();
    }

    private void setUpChoiceBox() {
        boolean currentUserRights = adminService.getUserEditRights();
        Session.setAdminAllowedEdit(currentUserRights);

        editRightsCheckBox.setSelected(currentUserRights); //UI

        editRightsCheckBox.selectedProperty().addListener((obs, old, newValue)->{ // ne na text property jer se onda ne prati sto admin tipka po checkboxu
            Session.setAdminAllowedEdit(newValue); // za session provjere
            handleUserRights(newValue); // za update baze podataka
        });
    }

    private void handleUserRights(Boolean newValue) {
        try{
            adminService.saveUserEditRights(newValue);
            LoggingUtil.logInfo("UPDATE", "User rights", Session.getCurrentUser().getUsername());
        }
        catch (CannotPerformActionException _){
            //ignore - interna evidancija je napravljena u servis sloju
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
        catch (CannotPerformActionException _){
            AlertUtil.showErrorAlert("Cannot perform this action at the moment");
        }
        clear();
    }

    private void clear() {
        searchField.clear();
    }

    @FXML
    public void onSeeDBStats(){
        try{
            Optional<DBStatistics>stats= adminService.getDBStats();

            if(stats.isPresent()){
                showStats(stats);
            }
        }
        catch (CannotPerformActionException _){
            AlertUtil.showErrorAlert("Cannot execute see database stats");
        }
    }

    private void showStats(Optional<DBStatistics> stats) {
        if(stats.isPresent()){
            headphonesCount.setText(stats.get().getHeadphonesEntriesNum() + "");
            brandsCount.setText(stats.get().getBrandEntriesNum() + "");
            techCount.setText(stats.get().getTechEntriesNum() + "");
            typesCount.setText(stats.get().getTypeEntriesNum() + "");
            usersCount.setText(stats.get().getUsersEntriesNum() + "");
            usesCount.setText(stats.get().getUseEntriesNum() + "");
        }
    }

    @FXML
    public void onDeleteAllEntities(){
        try{
            adminService.deleteAllEntities();
            LoggingUtil.logInfo("DELETE", "Entities",  Session.getCurrentUser().getUsername());
        }
        catch (CannotPerformActionException _){
            AlertUtil.showErrorAlert("Cannot delete all database content");
            //ovdje ne treba logger jer je taj exception vec zapisan u service sloju u logove, ovo je samo da user zna sto se dogodilo
        }
    }


    @FXML
    public void onClearStats(){
        headphonesCount.setText("");
        brandsCount.setText( "");
        techCount.setText("");
        typesCount.setText( "");
        usersCount.setText( "");
        usesCount.setText( "");
    }

    @FXML
    public void onDeleteUser(){
        try{
            if(selectedUser==null){
                AlertUtil.showErrorAlert("No user was selected for deletion");
                return;
            }
            if(!AlertUtil.showConfirmationAlert("Aou you sure you want to delete user?")){
                return;
            }

            adminService.deleteUserByUsername(selectedUser.getUsername());
        }
        catch (CannotPerformActionException _){
            AlertUtil.showErrorAlert("Cannot delete user");
        }
    }

    @FXML
    public void onDeleteAllUsers(){
        try{
            if(!AlertUtil.showConfirmationAlert("Are you sure you want to delete all users")){
                return;
            };
            adminService.deleteAllUsers();
        }
        catch (CannotPerformActionException _){
            AlertUtil.showErrorAlert("Cannot delete all users");
        }
    }

    @FXML
    public void onXMLBackup(){
        try{
            XMLExporter.marshall(headphonesService.getAllHeadphones());
            AlertUtil.showInformationAlert("Database to XML export successful");
        }
        catch (JAXBException e){
            LoggingUtil.logError("JAXB backup export error", e);
        }
    }

    @Override
    protected Node getMainNode() {
        return adminLabel;
    }
}
