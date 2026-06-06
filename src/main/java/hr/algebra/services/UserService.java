package hr.algebra.services;

import hr.algebra.exceptions.CannotPerformActionException;
import hr.algebra.exceptions.EntityExistantException;
import hr.algebra.models.Role;
import hr.algebra.models.Session;
import hr.algebra.models.User;
import hr.algebra.repositories.UserRepository;
import hr.algebra.repositories.factory.RepositoryFactory;
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

    private UserService() {
        this.repo= RepositoryFactory.getUserRespository();
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
            Session.setCurrentUser(user.get());
            return user;
        }
        return Optional.empty();
    }

    private boolean checkPasswordMatch(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
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
            throw new CannotPerformActionException("Cannot execute to register admin");
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
        //$2a$12$R9h/cIPz0gi.URNZX3khRecXSTu7pBw5ZKYT1T6m2wH2.Z50s4v32}$
        //algoritm, log rounds, 22 salt, cipher
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

    public void updateUser(User user){
        try{
            repo.save(user);
        }
        catch (SQLException e){
            LoggingUtil.logError("User update error", e);
            throw new CannotPerformActionException("Cannot execute to update user");
        }
    }

    public boolean usernameExists(String username){
        try{
            return repo.usernameExists(username);
        }
        catch (SQLException e){
            LoggingUtil.logError("Username existance checking error", e);
            throw new CannotPerformActionException("Cannot execute to check username existance");
        }
    }
}