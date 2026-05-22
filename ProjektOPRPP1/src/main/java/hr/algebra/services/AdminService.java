package hr.algebra.services;

import hr.algebra.exceptions.CannotPerformActionException;
import hr.algebra.repositories.AdminRepository;
import hr.algebra.repositories.implementation.AdminRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public enum AdminService {
    INSTANCE;
    private final AdminRepository repo = AdminRepositoryImpl.getInstance();
    private final Logger logger = LoggerFactory.getLogger(AdminService.class);

    public void deleteAllEntities(){
        try{
            repo.deleteAllEntities();
        }
        catch (SQLException e){
            logger.error("Cannot execute to delete all entities", e);
            throw new CannotPerformActionException("Cannot execute to delete all entities");
        }
    }

    public void deleteDatabase(){
        try{
            repo.deleteDatababase();
        }
        catch (SQLException e){
            logger.error("ERROR",e);
            throw new CannotPerformActionException("Cannot execute to delete database");
        }
    }

    public void deleteUser(String username){
        try{
            repo.deleteUser(username);
        }
        catch (SQLException e){
            logger.error("ERROR", e);
            throw new CannotPerformActionException("Cannot execute to delete user");
        }
    }

    public void deleteAllUsers(){
        try{
            repo.deleteAllUsers();
        }
        catch (SQLException e){
            logger.error("Cannot execute to delete all users", e);
            throw new CannotPerformActionException("Cannot execute to delete all users");
        }
    }
}
