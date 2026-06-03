package hr.algebra.models;

public class Session {

    private Session() {} // private da se ne moze instancirat nego samo koristit kao staticka klasa

    private static User currentUser;

    private static boolean adminAllowedEdit=true;

    public static void setAdminAllowedEdit(boolean adminAllowedEdit) {
        Session.adminAllowedEdit = adminAllowedEdit;
    }

    public  static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        Session.currentUser = currentUser;
    }

    public static boolean userCanEdit(){
        //admin uvijek moze
        return (userLoggedIn() && adminAllowedEdit) || currentUser.getRole()==Role.ADMIN;
    }

    public static boolean userLoggedIn(){
        return currentUser != null;
    }


}
