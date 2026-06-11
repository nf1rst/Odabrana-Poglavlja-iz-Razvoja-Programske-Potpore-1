package hr.algebra.controller;
import hr.algebra.exceptions.CannotPerformActionException;
import hr.algebra.models.Role;
import hr.algebra.models.help.DBStatistics;
import hr.algebra.models.Session;
import hr.algebra.models.User;
import hr.algebra.services.AdminService;
import hr.algebra.services.DBInitiatorService;
import hr.algebra.services.HeadphonesService;
import hr.algebra.services.UserService;
import hr.algebra.util.AlertUtil;
import hr.algebra.util.LoggingUtil;
import hr.algebra.util.MediaUtil;
import hr.algebra.util.XMLLoggingUtil;
import hr.algebra.xml.export.XMLExporter;
import jakarta.xml.bind.JAXBException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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

    ObservableList<User> masterList;

    private static final String DELETE ="DELETE"; // sonarqube
    @FXML
    public void initialize(){
        setUpColumns();
        setUpChoiceBox();

         masterList=FXCollections.observableArrayList(userService.getAllUsers());

        searchField.textProperty().addListener((obs, old, val)->{
            if(!val.isBlank()){
                observableListDataFill(userTableView, masterList, u->u.getUsername().contains(val));
            }
            else userTableView.setItems(masterList);
        });

        userTableView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, old, val)->{
                    if(val!=null){
                        selectedUser=val;
                    }
                });
        userTableView.setItems(masterList);
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
            XMLLoggingUtil.log("INFO", "UPDATE", "successfully changed user edit rights", "user", Session.getCurrentUser().getUsername(),  "*");
        }
        catch (CannotPerformActionException e){
            AlertUtil.showErrorAlert(e.getMessage());
        }
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
        userTableView.setItems(masterList);
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
        if(!AlertUtil.showConfirmationAlert("Admin, are you of sound mind and hereby swear you want to delete the contents of the database"))return;
        try{
            adminService.deleteAllEntities();
            MediaUtil.deleteImagesInIntelliJ();
            masterList.setAll(userService.getAllUsers());
            LoggingUtil.logInfo(DELETE, "Entities",  Session.getCurrentUser().getUsername());
            XMLLoggingUtil.log("INFO", DELETE, "successfully delted all entities", "*", Session.getCurrentUser().getUsername(),  "*");
        }
        catch (CannotPerformActionException _){
            AlertUtil.showErrorAlert("Cannot delete all database content");
            //ovdje ne treba logger jer je taj exception vec zapisan u service sloju u logove, ovo je samo da user zna sto se dogodilo
        }
        AlertUtil.showInformationAlert("Database content successfully disposed of");
    }

    @FXML
    public void onFillDB(){
        DBInitiatorService initiator = DBInitiatorService.INSTANCE;
        Task<Void> fillDb = new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                initiator.insetAPIData();
                return null;
            }
        };
        Thread thread = new Thread(fillDb);
        thread.setDaemon(true);
        thread.start();

        AlertUtil.showInformationAlert("Database successfully reinitialized with API data");
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
            if(selectedUser.getRole()== Role.ADMIN){
                AlertUtil.showErrorAlert("One man must be left standing");
                return;
            }

            adminService.deleteUserByUsername(selectedUser.getUsername());
            XMLLoggingUtil.log("INFO", DELETE, "successfully deleted", "user", Session.getCurrentUser().getUsername(),  selectedUser.getUsername());
            masterList.setAll(userService.getAllUsers());
            selectedUser=null;
        }
        catch (CannotPerformActionException _){
            AlertUtil.showErrorAlert("Cannot delete user");
        }
        userTableView.setItems(masterList);
    }

    @FXML
    public void onDeleteAllUsers(){
        try{
            if(!AlertUtil.showConfirmationAlert("Are you sure you want to delete all users")){
                return;
            }
            adminService.deleteAllUsers();
            masterList.setAll(userService.getAllUsers());
            XMLLoggingUtil.log("INFO", DELETE, "successfully delted all users", "user", Session.getCurrentUser().getUsername(),  "*");
        }
        catch (CannotPerformActionException _){
            AlertUtil.showErrorAlert("Cannot delete all users");
        }
        userTableView.setItems(masterList);
    }

    @FXML
    public void onXMLBackup(){
        try{
            XMLExporter.marshall(headphonesService.getAllHeadphones());
            AlertUtil.showInformationAlert("Database to XML export successful");
            XMLLoggingUtil.log("INFO", "EXPORT", "successfully exported DB backup", "*", Session.getCurrentUser().getUsername(),  "*");
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
