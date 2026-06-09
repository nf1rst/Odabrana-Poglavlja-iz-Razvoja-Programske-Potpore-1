package hr.algebra.repositories.implementation;

import hr.algebra.database.DataSource;
import hr.algebra.models.Use;
import hr.algebra.repositories.UseRepository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UseRepositoryImpl implements UseRepository {
    private static final UseRepositoryImpl INSTANCE=new UseRepositoryImpl();
    UseRepositoryImpl(){}
    public static UseRepositoryImpl getInstance(){return INSTANCE;}


    @Override
    public List<Use> getAll() throws SQLException {
        List<Use>uses=new ArrayList<>();
        try(Connection con = DataSource.getConnection();
            CallableStatement statement = con.prepareCall("{CALL GetAllUses()}");
            ResultSet rs  = statement.executeQuery())
        {
            while(rs.next())uses.add(mapRow(rs));
        }
        return uses;
    }

    private Use mapRow(ResultSet rs) throws SQLException {
        return new Use(rs.getInt("ID"),rs.getString("Name"));
    }

    @Override
    public Optional<Use> getById(int id) throws SQLException {
        try(Connection con =DataSource.getConnection();
                CallableStatement statement = con.prepareCall("{CALL GetUseByID(?)}")){
            statement.setInt(1,id);
            try(ResultSet rs = statement.executeQuery()){
                if(rs.next()) Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public void delete(int id) throws SQLException {
        try(Connection con =DataSource.getConnection();
                CallableStatement statement = con.prepareCall("{CALL DeleteUse(?)}")){
            statement.setInt(1,id);
            statement.execute();
        }
    }

    @Override
    public void save(Use use) throws SQLException {
        if(use.getId()==0)insert(use);
        else update(use);
    }

    private void update(Use use) throws SQLException {
        try(Connection con =DataSource.getConnection();
                CallableStatement statement = con.prepareCall("{CALL UpdateUse(?,?)}")){
            statement.setInt(1,use.getId());
            statement.setString(2,use.getName());
            statement.execute();
        }
    }

    private void insert(Use use) throws SQLException {
        try(Connection con =DataSource.getConnection();
                CallableStatement statement = con.prepareCall("{CALL InsertUse(?)}")){
            statement.setString(1,use.getName());
            try(ResultSet rs = statement.executeQuery()){
                if(rs.next()){
                    int generatedID = rs.getInt("generatedID");
                    use.setId(generatedID);
                }
            }
        }
    }

    @Override
    public int getUseIdByName(String name) throws SQLException {
        try(Connection con =DataSource.getConnection();
                CallableStatement statement = con.prepareCall("{CALL GetUseIdByName(?)}")){
            statement.setString(1, name);
            try(ResultSet rs = statement.executeQuery()){
                if(rs.next()){return rs.getInt("id");}
            }
        }
        return 0;
    }
}
