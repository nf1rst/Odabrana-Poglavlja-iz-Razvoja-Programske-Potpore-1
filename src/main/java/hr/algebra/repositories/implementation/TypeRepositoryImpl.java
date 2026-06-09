package hr.algebra.repositories.implementation;

import hr.algebra.database.DataSource;
import hr.algebra.models.Type;
import hr.algebra.repositories.TypeRepository;

import java.sql.CallableStatement;
import java.sql.Connection;
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
    public List<Type> getAll() throws SQLException {
        List<Type>types = new ArrayList<>();
        try(Connection con = DataSource.getConnection();
            CallableStatement statement = con.prepareCall("{CALL GetAllTypes()}");
            ResultSet rs = statement.executeQuery())
        {
            while(rs.next())types.add(mapRow(rs));
        }
        return types;
    }


    @Override
    public Optional<Type> getById(int id) throws SQLException {
        try(Connection con =DataSource.getConnection();
                CallableStatement statement = con.prepareCall("{CALL GetTypeById(?)}")){
            statement.setInt(1,id);
            try(ResultSet rs = statement.executeQuery()){
                if(rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public void delete(int id) throws SQLException {
        try(Connection con =DataSource.getConnection();
                CallableStatement statement = con.prepareCall("{CALL DeleteType(?)}")){
            statement.setInt(1,id);
            statement.execute();
        }
    }

    @Override
    public void save(Type type) throws SQLException {
        if(type.getId()==0)insert(type); // 0 jer sam ja tako stavila u kontruktoru pa ga moze prepoznat
        else update(type);
    }

    private void update(Type type) throws SQLException {
        try(Connection con =DataSource.getConnection();
                CallableStatement statement = con.prepareCall("{CALL UpdateType(?,?)}")){
            statement.setInt(1,type.getId());
            statement.setString(2,type.getName());
            statement.execute();
        }
    }

    private void insert(Type type) throws SQLException {
        try(Connection con =DataSource.getConnection();
                CallableStatement statement = con.prepareCall("{CALL InsertType(?)}")){
            statement.setString(1,type.getName());

            try(ResultSet rs = statement.executeQuery()){
                if(rs.next()){
                    int generatedID = rs.getInt("generatedID");
                    type.setId(generatedID);
                }
            }

        }
    }

    @Override
    public int getTypeIdByName(String name) throws SQLException {
        try(Connection con =DataSource.getConnection();
                CallableStatement statement = con.prepareCall("{CALL GetTypeIdByName(?)}")){
            statement.setString(1, name);
            try(ResultSet rs = statement.executeQuery()){
                if(rs.next()){return rs.getInt("id");}
            }
        }
        return 0;
    }

    private Type mapRow(ResultSet rs) throws SQLException {
        return new Type(rs.getInt("id"), rs.getString("name"));
    }

}
