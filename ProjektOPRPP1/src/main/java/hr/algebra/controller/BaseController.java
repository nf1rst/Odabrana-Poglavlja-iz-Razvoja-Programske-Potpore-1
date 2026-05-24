package hr.algebra.controller;
import hr.algebra.models.Session;
import hr.algebra.models.User;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseController {

    protected User currentUser = Session.getCurrentUser();


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
