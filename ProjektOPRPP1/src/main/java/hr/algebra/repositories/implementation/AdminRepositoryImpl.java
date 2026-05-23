package hr.algebra.repositories.implementation;

import hr.algebra.database.DBConnector;
import hr.algebra.models.DBStatistics;
import hr.algebra.repositories.AdminRepository;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AdminRepositoryImpl implements AdminRepository {
    private AdminRepositoryImpl() {
    }

    private static final AdminRepositoryImpl INSTANCE = new AdminRepositoryImpl();

    public static AdminRepositoryImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public void deleteUser(String username) throws SQLException {
        try (CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL DeleteUserByUsername(?)}");) {
            statement.setString(1, username);
            statement.execute();
        }
    }

    @Override
    public void deleteAllEntities() throws SQLException {
        try (CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL DeleteAllEntities()}")) {
            statement.execute();
        }

    }

    @Override
    public void deleteDatababase() throws SQLException {
        try (CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL DropDatabase()}")) {
            statement.execute();
        }
    }

    @Override
    public void deleteAllUsers() throws SQLException {
        try (CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL DeleteAllUsers()}")) {
            statement.execute();
        }

    }

    @Override
    public Optional<DBStatistics> getDBStats() throws SQLException {
        try (CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL GetNumberOfEntries()}")) {
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) return Optional.of(mapStatistics(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public void saveUserEditRights(boolean value) throws SQLException {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL SetUserEditRights(?) }")){
            statement.setBoolean(1, value);
            statement.execute();
        }
    }

    @Override
    public boolean getUserEditRights() throws SQLException {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL GetUserEditRights()}")){
            try(ResultSet rs = statement.executeQuery()){
                if(rs.next())return rs.getBoolean(1);
            }
        }
        return false;
    }

    private DBStatistics mapStatistics(ResultSet rs) throws SQLException {
        //headphones, brand, technology, use, type, users
        return new DBStatistics(
                rs.getInt("headphonesNum"),
                rs.getInt("brandNum"),
                rs.getInt("techNum"),
                rs.getInt("useNum"),
                rs.getInt("typeNum"),
                rs.getInt("usersNum")
        );
    }
}
