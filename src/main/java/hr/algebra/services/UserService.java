package hr.algebra.services;
import hr.algebra.exceptions.CannotPerformActionException;
import hr.algebra.exceptions.EntityExistantException;
import hr.algebra.models.Role;
import hr.algebra.models.User;
import hr.algebra.repositories.UserRepository;
import hr.algebra.repositories.implementation.UserRepositoryImpl;
import hr.algebra.util.LoggingUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserService {
    //singleton
    private static final UserService INSTANCE=new UserService();
    public static UserService getInstance(){return INSTANCE;}

    private final UserRepository repo;
    private User loggedUser;

    private UserService() {
        this.repo= UserRepositoryImpl.getInstance();
    }

    public Optional<User> login(String username, String password){
        //https://davidbertoldi.medium.com/hashing-passwords-in-java-757e787ce71c
        //https://medium.com/@singhalabhay19/understanding-bcrypt-in-java-a-deep-dive-into-password-hashing-1b4362ccae94

        Optional<User> user=Optional.empty();
        try{
            user = repo.getByUsername(username);
        }
        catch (SQLException e){
            LoggingUtil.logError("Get by username error", e);
            throw new CannotPerformActionException("Cannot execute to find user by username");
        }

        if(user.isEmpty()){
            throw new EntityExistantException("User does not exist");
        }

        boolean passwordsMatch = checkPasswordMatch(password, user.get().getPassword());

        if(passwordsMatch){
            loggedUser=user.get();
            return user;
        }
        return Optional.empty();
    }

    private boolean checkPasswordMatch(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    public void logOut(){
        this.loggedUser=null;
    }

    public Optional<User> getLoggedUser(){
        return Optional.ofNullable(loggedUser);
    }


    public void registerAdmin(String username, String password){
        try{
            if(repo.usernameExists(username))throw new EntityExistantException("Admin already exists");
            String passwordHash = hashPassword(password);
            User admin = new User(username,passwordHash, Role.ADMIN);
            repo.save(admin);
        }
        catch (SQLException e){
            LoggingUtil.logError("User save error", e);
            LoggingUtil.logError("Cannot execute to register admin", e);
        }
    }

    public void register(String username,String password){
        try{
            if(repo.usernameExists(username))throw new EntityExistantException("Username already in use");
        }
        catch (SQLException e){ // provjera jel se procedura opce moze
            LoggingUtil.logError("Username exists error", e);
            throw new  CannotPerformActionException("Cannot execute to register user");
        }
        //ovdje ne treba provjera inputa jer je to napravljeno u kontrolleru

        String hashedPassword = hashPassword(password);
        User newUser = new User(username,hashedPassword,Role.USER);

        try{
            repo.save(newUser);
        }
        catch (SQLException e){
            LoggingUtil.logError("User save error", e);
            throw new CannotPerformActionException("Cannot execute to save user");
        }
    }

    private String hashPassword(String password) {
        int logRounds=12;
        String salt = BCrypt.gensalt(logRounds);
        return BCrypt.hashpw(password, salt);
    }

    public List<User> getAllUsers(){
        try{
            return repo.getAll();
        }
        catch (SQLException e){
            LoggingUtil.logError("Get all users error", e);
            throw new CannotPerformActionException("Cannot execute to get users");
        }
    }

    public List<User> search(String query){
        try{
            if(query==null || query.isEmpty()){return repo.getAll();}
            return repo.getAll()
                    .stream()
                    .filter(s->s.getUsername().contains(query))
                    .toList();
        }
        catch (SQLException e){
            LoggingUtil.logError("Get all users error", e);
            throw new CannotPerformActionException("Cannot execute to get users");
        }

    }

    public void deleteUser(int id){
        try{
            repo.delete(id);
        }
        catch (SQLException e){
            LoggingUtil.logError("User delete error", e);
            throw new CannotPerformActionException("Cannot execute to delete user");
        }
    }

    public void updateImagePath(User user, String imagePath){
        try{
            user.setImagePath(imagePath);
            repo.save(user);
        }
        catch (SQLException e){
            LoggingUtil.logError("User update image error", e);
            throw new CannotPerformActionException("Cannot execute to update image for user");
        }
    }

    public void updateUser(User user){
        try{
            repo.save(user);
        }
        catch (SQLException e){
            LoggingUtil.logError("User update error", e);
            throw new CannotPerformActionException("Cannot execute to update user");
        }
    }

}