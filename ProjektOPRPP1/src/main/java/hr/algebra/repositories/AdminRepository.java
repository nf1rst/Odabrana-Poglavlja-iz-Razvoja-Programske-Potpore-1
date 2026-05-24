package hr.algebra.repositories;

import hr.algebra.models.DBStatistics;

import java.sql.SQLException;
import java.util.Optional;

public interface AdminRepository {
    void deleteUser(String username) throws SQLException;
    void deleteAllEntities() throws SQLException;
    void dropAllTables() throws SQLException;
    void deleteAllUsers() throws SQLException;
    Optional<DBStatistics> getDBStats() throws SQLException;
    void saveUserEditRights(boolean value) throws SQLException;
    boolean getUserEditRights() throws SQLException;
}
