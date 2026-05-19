package hr.algebra.services;
import hr.algebra.exceptions.EntityExistantException;
import hr.algebra.models.Role;
import hr.algebra.models.User;
import hr.algebra.repositories.UserRepository;
import hr.algebra.repositories.implementation.UserRepositoryImpl;
import org.mindrot.jbcrypt.BCrypt;
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

        Optional<User> user = repo.getByUsername(username);

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



    public void register(String username,String password){
        if(repo.usernameExists(username))throw new EntityExistantException("Username already in use");
        //ovdje ne treba provjera inputa jer je to napravljeno u kontrolleru

        String hashedPassword = hashPassword(password);
        User newUser = new User(username,hashedPassword,Role.USER);
        repo.save(newUser);
    }

    private String hashPassword(String password) {
        int logRounds=12;
        String salt = BCrypt.gensalt(logRounds);
        return BCrypt.hashpw(password, salt);
    }

    public List<User> getAllUsers(){
        return repo.getAll();
    }

    public void deleteUser(int id){repo.delete(id);}

    public void updateImagePath(User user, String imagePath){
        user.setImagePath(imagePath);
        repo.save(user);
    }

    public void updateUser(User user){
        repo.save(user);
    }

}