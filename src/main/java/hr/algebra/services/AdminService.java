package hr.algebra.services;

import hr.algebra.exceptions.CannotPerformActionException;
import hr.algebra.models.help.DBStatistics;
import hr.algebra.repositories.AdminRepository;
import hr.algebra.repositories.factory.RepositoryFactory;
import hr.algebra.util.LoggingUtil;

import java.sql.SQLException;
import java.util.Optional;

public enum AdminService {
    INSTANCE;
    private final AdminRepository repo = RepositoryFactory.getAdminRespository();

    private String sqlError="SQL error";
    public void deleteAllEntities() {
        try {
            repo.deleteAllEntities();
        } catch (SQLException e) {
            LoggingUtil.logError(sqlError, e);
            throw new CannotPerformActionException("Cannot execute to delete all entities");
        }
    }

    public void deleteUserByUsername(String username) {
        try {
            repo.deleteUserByUsername(username);
        } catch (SQLException e) {
            LoggingUtil.logError(sqlError, e);
            throw new CannotPerformActionException("Cannot execute to delete user");
        }
    }

    public void deleteAllUsers() {
        try {
            repo.deleteAllUsers();
        } catch (SQLException e) {
            LoggingUtil.logError(sqlError, e);
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
            LoggingUtil.logError(sqlError, e);
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
