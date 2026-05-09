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
    public List<Technology> getAll() {
        List<Technology>technology=new ArrayList<>();
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL GetAllTechnologies()}");
            ResultSet rs = statement.executeQuery();)
        {
            while(rs.next())technology.add(mapRow(rs));
        }
        catch(SQLException e){
            throw new RuntimeException("Cannot get technologies", e);
        }
        return technology;
    }

    @Override
    public Optional<Technology> getById(int id) {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL GetTechnologyById(?)}")){
            statement.setInt(1,id);
            try(ResultSet rs=statement.executeQuery()){
                while(rs.next())Optional.of(mapRow(rs));
            }
        }
        catch(SQLException e){
            throw new RuntimeException("Cannot get technology", e);
        }
        return Optional.empty();
    }

    @Override
    public void delete(int id) {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL RemoveTechnology(?)}")){
            statement.setInt(1,id);
            statement.execute();
        }
        catch(SQLException e){
            throw new RuntimeException("Cannot delete technology", e);
        }
    }

    @Override
    public void save(Technology technology) {
        if(technology.getId()==0)insert(technology);
        else update(technology);
    }

    private void update(Technology technology) {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL UpdateTechnology(?,?,?)}")){
            statement.setInt(1,technology.getId());
            statement.setString(2,technology.getName());
            statement.setString(3,technology.getDescription());
        }
        catch (SQLException e){
            throw new RuntimeException("Cannot update technology", e);
        }
    }

    private void insert(Technology technology) {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL InsertTechnology(?,?)}")){
            statement.setString(1,technology.getName());
            statement.setString(2,technology.getDescription());
            statement.execute();
        }
        catch(SQLException e){
            throw new RuntimeException("Cannot insert technology", e);
        }
    }

    private Technology mapRow(ResultSet rs) throws SQLException {
        return new Technology(rs.getInt("id"), rs.getString("name"), rs.getString("description"));
    }
}
