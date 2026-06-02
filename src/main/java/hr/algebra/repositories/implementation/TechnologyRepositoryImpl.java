package hr.algebra.repositories.implementation;
import hr.algebra.database.DBConnector;
import hr.algebra.models.Technology;
import hr.algebra.repositories.TechnologyRepository;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TechnologyRepositoryImpl implements TechnologyRepository {
    private static final TechnologyRepositoryImpl INSTANCE=new TechnologyRepositoryImpl();
    TechnologyRepositoryImpl(){}
    public static TechnologyRepositoryImpl getInstance(){return INSTANCE;}

    @Override
    public List<Technology> getAll() throws SQLException {
        List<Technology>technology=new ArrayList<>();
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL GetAllTechnologies()}");
            ResultSet rs = statement.executeQuery();)
        {
            while(rs.next())technology.add(mapRow(rs));
        }
        return technology;
    }

    @Override
    public Optional<Technology> getById(int id) throws SQLException {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL GetTechnologyById(?)}")){
            statement.setInt(1,id);
            try(ResultSet rs=statement.executeQuery()){
                while(rs.next())Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public void delete(int id) throws SQLException {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL RemoveTechnology(?)}")){
            statement.setInt(1,id);
            statement.execute();
        }
    }

    @Override
    public void save(Technology technology) throws SQLException {
        if(technology.getId()==0)insert(technology);
        else update(technology);
    }

    private void update(Technology technology) throws SQLException {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL UpdateTechnology(?,?,?)}")){
            statement.setInt(1,technology.getId());
            statement.setString(2,technology.getName());
            statement.setString(3,technology.getDescription());
        }
    }

    private void insert(Technology technology) throws SQLException {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL InsertTechnology(?,?)}")){
            statement.setString(1,technology.getName());
            statement.setString(2,technology.getDescription());

            try(ResultSet rs = statement.executeQuery()){
                if(rs.next()){
                    int generatedID = rs.getInt("generatedID");
                    technology.setId(generatedID);
                }
            }
        }

    }

    @Override
    public int getTechnologydIdByName(String name) throws SQLException {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL GetTechnologyIdByName(?)}")){
            statement.setString(1, name);
            try(ResultSet rs = statement.executeQuery()){
                if(rs.next()){return rs.getInt("id");}
            }
        }
        return 0;
    }

    private Technology mapRow(ResultSet rs) throws SQLException {
        return new Technology(rs.getInt("id"), rs.getString("name"), rs.getString("description"));
    }
}
