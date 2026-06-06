package hr.algebra.controller;

import hr.algebra.exceptions.CannotPerformActionException;
import hr.algebra.models.Session;
import hr.algebra.models.User;
import hr.algebra.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;

import java.util.List;

public abstract class BaseController {

    protected User currentUser = Session.getCurrentUser();

    protected <T> void fillTable(TableView<T>tableView, List<T> list){
        try{
            tableView.setItems(FXCollections.observableArrayList(list));
            tableView.getSelectionModel().clearSelection();
        }
        catch (CannotPerformActionException e){
            AlertUtil.showErrorAlert(e.getMessage());
        }
    }

    protected void onUserSet() { //prazna metoda koju klasa koja ovu nsaljeduje moze overrideat ako joj je potrebno da se nesto dogodi nakon postavljanja usera
    //ko onaj default metod u interafceima
    }

    protected abstract Node getMainNode(); // svaka podklasa mora dat bilo koji node iz forme da se moze dohvatit window i zatvorit prozor

    protected void fillStatusLabel(Label label, String message, Color color) {
        label.setText(message);
        label.setTextFill(color);
    }

    protected void fillUserLabel(Label label) {
        if(!Session.userLoggedIn()) label.setText("Anonymous");
        else label.setText(currentUser.getUsername());
    }

}
