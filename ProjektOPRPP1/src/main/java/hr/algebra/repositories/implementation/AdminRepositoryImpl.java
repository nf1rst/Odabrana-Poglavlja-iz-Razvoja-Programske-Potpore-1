package hr.algebra.repositories.implementation;

import hr.algebra.database.DBConnector;
import hr.algebra.repositories.AdminRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminRepositoryImpl implements AdminRepository {
    private AdminRepositoryImpl() {}
    private static final AdminRepositoryImpl INSTANCE = new AdminRepositoryImpl();

    public static AdminRepositoryImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public void deleteUser(String username) throws SQLException {
        try(PreparedStatement statement = DBConnector.getConnection().prepareCall("{CALL DeleteUserByUsername(?)}");){
            statement.setString(1,username);
            statement.execute();
        }
    }

    @Override
    public void deleteAllEntities() throws SQLException {
        try(PreparedStatement statement = DBConnector.getConnection().prepareCall("{CALL DeleteAllEntities()}")){
            statement.execute();
        }

    }

    @Override
    public void deleteDatababase() throws SQLException {
        try(PreparedStatement statement = DBConnector.getConnection().prepareCall("{CALL DropDatabase()}")){
            statement.execute();
        }
    }

    @Override
    public void deleteAllUsers() throws SQLException {
        try(PreparedStatement statement = DBConnector.getConnection().prepareCall("{CALL DeleteAllUsers()}")){
            statement.execute();
        }

    }
}
