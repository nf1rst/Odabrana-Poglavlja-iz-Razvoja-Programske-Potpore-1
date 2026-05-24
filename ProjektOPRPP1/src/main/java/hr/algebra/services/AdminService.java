package hr.algebra.services;

import hr.algebra.exceptions.CannotPerformActionException;
import hr.algebra.models.DBStatistics;
import hr.algebra.repositories.AdminRepository;
import hr.algebra.repositories.implementation.AdminRepositoryImpl;
import hr.algebra.util.LoggingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Optional;

public enum AdminService {
    INSTANCE;
    private final AdminRepository repo = AdminRepositoryImpl.getInstance();
    private final Logger logger = LoggerFactory.getLogger(AdminService.class);

    public void deleteAllEntities() {
        try {
            repo.deleteAllEntities();
        } catch (SQLException e) {
            logger.error("Cannot execute to delete all entities", e);
            throw new CannotPerformActionException("Cannot execute to delete all entities");
        }
    }

    public void dropAllTables() {
        try {
            repo.dropAllTables();
        } catch (SQLException e) {
            logger.error("ERROR", e);
            throw new CannotPerformActionException("Cannot execute to delete database");
        }
    }

    public void deleteUser(String username) {
        try {
            repo.deleteUser(username);
        } catch (SQLException e) {
            logger.error("ERROR", e);
            throw new CannotPerformActionException("Cannot execute to delete user");
        }
    }

    public void deleteAllUsers() {
        try {
            repo.deleteAllUsers();
        } catch (SQLException e) {
            logger.error("Cannot execute to delete all users", e);
            throw new CannotPerformActionException("Cannot execute to delete all users");
        }
    }

    public Optional<DBStatistics> getDBStats() {
        try {
            Optional<DBStatistics> stats = repo.getDBStats();
            if (stats.isPresent()) {
                return stats;
            }
        } catch (SQLException e) {
            logger.error("Cannot execute to get DB stats", e);
            throw new CannotPerformActionException("Cannot execute to get DB stats");
        }
        return Optional.empty();
    }

    public void saveUserEditRights(boolean value){
        try{
            repo.saveUserEditRights(value);
        }
        catch (SQLException e){
            LoggingUtil.logError("Cannot execute to save user rights", e);
            throw new CannotPerformActionException("Cannot  execute to save user rights");
        }
    }

    public boolean getUserEditRights(){
        try{
            return repo.getUserEditRights();
        }
        catch (SQLException e){
            LoggingUtil.logError("Cannot get user rights", e);
            throw new CannotPerformActionException("Cannot execute to get user rights");
        }
    }
}
