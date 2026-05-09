package hr.algebra.repositories.implementation;
import hr.algebra.database.DBConnector;
import hr.algebra.models.Type;
import hr.algebra.repositories.TypeRepository;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TypeRepositoryImpl implements TypeRepository {
    private static final TypeRepositoryImpl INSTANCE=new TypeRepositoryImpl();//eager loading
    public static TypeRepositoryImpl getInstance(){return INSTANCE;}

    TypeRepositoryImpl(){}

    @Override
    public List<Type> getAll() {
        List<Type>types = new ArrayList<>();
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL GetAllTypes()}");
            ResultSet rs = statement.executeQuery())
        {
            while(rs.next())types.add(mapRow(rs));
        }
        catch(SQLException e){
            throw new RuntimeException("Cannot get types", e);
        }
        return types;
    }


    @Override
    public Optional<Type> getById(int id) {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL GetTypeById(?)}")){
            statement.setInt(1,id);
            try(ResultSet rs = statement.executeQuery()){
                while(rs.next()) return Optional.of(mapRow(rs));
            }
        }
        catch(SQLException e){
            throw new RuntimeException("Cannot get type", e);
        }
        return Optional.empty();
    }

    @Override
    public void delete(int id) {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL DeleteType(?)}")){
            statement.setInt(1,id);
            statement.execute();
        }
        catch(SQLException e){
            throw new RuntimeException("Cannot delete type", e);
        }
    }

    @Override
    public void save(Type type) {
        if(type.getId()==0)insert(type); // 0 jer sam ja tako stavila u kontruktoru pa ga moze prepoznat
        else update(type);
    }

    private void update(Type type) {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL UpdateType(?,?)}")){
            statement.setInt(1,type.getId());
            statement.setString(2,type.getName());
            statement.execute();
        }
        catch(SQLException e){
            throw new RuntimeException("Cannot update type", e);
        }
    }

    private void insert(Type type) {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL InsertType(?)}")){
            statement.setString(1,type.getName());
            statement.execute();
        }
        catch(SQLException e){
            throw new RuntimeException("Cannot insert type", e);
        }
    }

    private Type mapRow(ResultSet rs) throws SQLException {
        return new Type(rs.getInt("id"), rs.getString("name"));
    }

}
