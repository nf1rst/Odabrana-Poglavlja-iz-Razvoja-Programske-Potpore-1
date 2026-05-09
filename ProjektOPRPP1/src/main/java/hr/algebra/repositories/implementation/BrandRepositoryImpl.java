package hr.algebra.repositories.implementation;
import hr.algebra.database.DBConnector;
import hr.algebra.models.Brand;
import hr.algebra.repositories.BrandRepository;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BrandRepositoryImpl implements BrandRepository {
    private static final BrandRepositoryImpl INSTANCE = new BrandRepositoryImpl();
    public static BrandRepositoryImpl getInstance(){return INSTANCE;}//eager loading
    private BrandRepositoryImpl() {}

    @Override
    public List<Brand> getAll() {
        List<Brand>brands = new ArrayList<>();
        try (
                CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL GetAllBrands()}");
                ResultSet rs = statement.executeQuery())
        {
            while (rs.next())brands.add(mapRow(rs));
        }
        catch(SQLException e){
            throw new RuntimeException("Cannot fetch the brands", e); // jel ovo potrebno bacat ponovno ili sto se treba, tko ce ovo hvatat
        }
        return brands;
    }

    @Override
    public Optional<Brand> getById(int id) {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL GetBrandById(?)}")){
            statement.setInt(1,id);
            try(ResultSet rs = statement.executeQuery()){
                if(rs.next())return Optional.of(mapRow(rs));
            }
        }
        catch (SQLException e){
            throw new RuntimeException("Cannot fetch the brand",e);
        }
        return Optional.empty();
    }

    @Override
    public void delete(int id) {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL DeleteBrand(?)}")){
            statement.setInt(1,id);
            statement.execute();
        }
        catch(SQLException e){
            throw new RuntimeException("Cannot delete the brand",e);
        }
    }

    @Override
    public void save(Brand brand) {
        if(brand.getId()==0) insert(brand);
        else update(brand);
    }

    private void update(Brand brand) {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL UpdateBrand(?,?,?)}")){
            statement.setInt(1,brand.getId());
            statement.setString(2,brand.getName());
            statement.setString(3,brand.getDescription());
            statement.execute();
        }
        catch(SQLException e){
            throw new RuntimeException("Cannot update brand", e);
        }
    }

    private void insert (Brand brand){
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL InsertBrand(?,?)}")){
            statement.setString(1,brand.getName());
            statement.setString(2,brand.getDescription());
            statement.execute();
        }
        catch(SQLException e){
            throw new RuntimeException("Cannot insert brand", e);
        }
    }


    private Brand mapRow(ResultSet rs) throws SQLException {
        return new Brand(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description")
        );
    }
}
