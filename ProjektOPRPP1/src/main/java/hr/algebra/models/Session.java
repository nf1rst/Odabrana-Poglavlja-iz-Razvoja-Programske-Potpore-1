package hr.algebra.models;

public class Session {

    private Session() {} // private da se ne moze instancirat nego samo koristit kao staticka klasa

    private static User currentUser;

    private static boolean adminAllowedEdit=true;

    public  static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        Session.currentUser = currentUser;
    }

    public static boolean userCanEdit(){
        //admin uvijek moze
        if(!userLoggedIn() || adminAllowedEdit==false)return false; // i ako admin kaze da moze
        return true;
    }

    public static boolean userLoggedIn(){
        if(currentUser==null)return false;
        return true;
    }


}
