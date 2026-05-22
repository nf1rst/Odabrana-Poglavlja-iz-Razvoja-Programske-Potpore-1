package hr.algebra.repositories;

import java.sql.SQLException;

public interface AdminRepository {
    void deleteUser(String username) throws SQLException;
    void deleteAllEntities() throws SQLException;
    void deleteDatababase() throws SQLException;
    void deleteAllUsers() throws SQLException;
}
