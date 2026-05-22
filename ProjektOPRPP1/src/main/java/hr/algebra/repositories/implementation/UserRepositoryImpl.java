package hr.algebra.repositories.implementation;
import hr.algebra.database.DBConnector;
import hr.algebra.exceptions.CannotPerformActionException;
import hr.algebra.models.Role;
import hr.algebra.models.User;
import hr.algebra.repositories.UserRepository;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private static final UserRepositoryImpl INSTANCE=new UserRepositoryImpl();
    private UserRepositoryImpl(){}
    public static UserRepositoryImpl getInstance(){return INSTANCE;}

    @Override
    public Optional<User> getByUsername(String username) throws SQLException {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL GetUserByUsername(?)}"))
        {
            statement.setString(1,username);
            try(ResultSet rs = statement.executeQuery()){
                if(rs.next())return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean usernameExists(String username) throws SQLException {
        return getByUsername(username).isPresent();
    }

    @Override
    public List<User> getAll() throws SQLException {
        List<User> users=new ArrayList<>();
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL GetAllUsers()}");
            ResultSet rs = statement.executeQuery();)
        {
            while(rs.next())users.add(mapRow(rs));
        }
        return users;
    }

    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("Id"),
                rs.getString("Username"),
                rs.getString("Password"),
                Role.valueOf(rs.getString("Role")),
                rs.getString("ImagePath")
        );
    }
    @Override
    public Optional<User> getById(int id) throws SQLException {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL GetUserById(?)}")){
            statement.setInt(1, id);
            try(ResultSet rs = statement.executeQuery()){
                if(rs.next())return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    public void update(User user) throws SQLException {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL UpdateUser(?,?,?,?, ?)}")){
            statement.setInt(1,user.getId());
            statement.setString(2,user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRole().name());
            statement.setString(5,user.getImagePath());
            statement.execute();
        }
    }

    private void insert(User user) throws SQLException {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL InsertUser(?,?,?, ?)}")){
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().name());//name jer inace ne moze vratit string kad ovo daje moj custom Role
            statement.setString(4, user.getImagePath());
            statement.execute();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL DeleteUser(?)}")){
            statement.setInt(1,id);
            statement.execute();
        }
    }

    @Override
    public void save(User user) throws SQLException {
        if(user.getId()==0)insert(user);
        else update(user);
    }
}
