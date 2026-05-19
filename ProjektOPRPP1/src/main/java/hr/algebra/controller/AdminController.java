package hr.algebra.controller;

import hr.algebra.models.User;
import javafx.scene.Node;

public class AdminController extends BaseController{
    private User currentser;
    public void setUser(User user) {
        this.currentser=user;
    }

    @Override
    protected Node getMainNode() {
        return null;
    }
}
