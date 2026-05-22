package hr.algebra.controller;
import hr.algebra.models.Session;
import hr.algebra.models.User;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseController {

    protected static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected User currentUser = Session.getCurrentUser();


    protected void onUserSet() { //prazna metoda koju klasa koja ovu nsaljeduje moze overrideat ako joj je potrebno da se nesto dogodi nakon postavljanja usera
    //ko onaj default metod u interafceima
    }

    protected abstract Node getMainNode(); // svaka podklasa mora dat bilo koji node iz forme da se moze dohvatit window i zatvorit prozor

    protected String checkUsernameForLogging() {// ako korisnik nije ulogiran da ne pukne
        return currentUser.getUsername()!=null? currentUser.getUsername():"N/A";
    }

    //https://stackoverflow.com/questions/41233886/display-a-confirmation-dialogue-when-the-user-try-to-exit-the-application-press


    protected void fillUserLabel(Label label) {
        if(!Session.userLoggedIn()) label.setText("Anonymous");
        else label.setText(currentUser.getUsername());
    }

}
