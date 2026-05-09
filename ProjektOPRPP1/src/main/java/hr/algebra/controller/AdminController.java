package hr.algebra.controller;

import hr.algebra.models.User;

public class AdminController {
    private User currentser;
    public void setUser(User user) {
        this.currentser=user;
    }
}
