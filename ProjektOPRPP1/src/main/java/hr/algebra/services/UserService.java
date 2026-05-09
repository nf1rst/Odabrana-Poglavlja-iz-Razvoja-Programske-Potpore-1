package hr.algebra.services;
import hr.algebra.models.Role;
import hr.algebra.models.User;
import hr.algebra.repositories.UserRepository;
import hr.algebra.repositories.implementation.UserRepositoryImpl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

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
        Optional<User>user = repo.getByUsername(username)
                .filter(u->u.getPassword().equals(password)); //predikat
        user.ifPresent(u->loggedUser=u); //ako je tocno onda ga spremi kao da se ulogirao
        return user;
    }

    public void logOut(){
        this.loggedUser=null;
    }

    public Optional<User> getLoggedUser(){
        return Optional.ofNullable(loggedUser);
    }

    public boolean isLoggedUserAdmin(){
        return getLoggedUser().map(u->u.getRole()== Role.ADMIN)
                .orElse(false);
        //zasto ne samo loggedUser.GetRole()==ROLE.ADMIN
    }


    //registracija kompletno novog korisnika - ako korisnik postoji false, ako ne postoji kreiraj ga i vrati true

    public boolean register(String username,String password){
        if(repo.usernameExists(username))return false; //korisnik vec postoji

      Predicate<String> validUsername = s->s!=null && s.trim().length()>=5;
      Predicate<String> valudPassword = s->s!=null && s.trim().length()>=7;

        if(!validUsername.test(username) || !valudPassword.test(password))return false;

        User newUser = new User(username,password,Role.USER);
        repo.save(newUser);

        return true;
    }

    public List<User> getAllUsers(){
        return repo.getAll();
    }

    public void deleteUser(int id){repo.delete(id);}

    public void updateImagePath(User user, String imagePath){
        user.setImagePath(imagePath);
        repo.save(user);
    }

}
