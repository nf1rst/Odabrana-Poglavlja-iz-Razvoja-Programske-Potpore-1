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
    public List<Brand> getAll() throws SQLException {
        List<Brand>brands = new ArrayList<>();
        try (
                CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL GetAllBrands()}");
                ResultSet rs = statement.executeQuery())
        {
            while (rs.next())brands.add(mapRow(rs));
        }
        return brands;
    }

    @Override
    public Optional<Brand> getById(int id) throws SQLException {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL GetBrandById(?)}")){
            statement.setInt(1,id);
            try(ResultSet rs = statement.executeQuery()){
                if(rs.next())return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public void delete(int id) throws SQLException {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL DeleteBrand(?)}")){
            statement.setInt(1,id);
            statement.execute();
        }
    }

    @Override
    public void save(Brand brand) throws SQLException {
        if(brand.getId()==0) insert(brand);
        else update(brand);
    }

    private void update(Brand brand) throws SQLException {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL UpdateBrand(?,?,?)}")){
            statement.setInt(1,brand.getId());
            statement.setString(2,brand.getName());
            statement.setString(3,brand.getDescription());
            statement.execute();
        }
    }

    private void insert (Brand brand) throws SQLException {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL InsertBrand(?,?)}")){
            statement.setString(1,brand.getName());
            statement.setString(2,brand.getDescription());
            try(ResultSet rs = statement.executeQuery()){
                if(rs.next()){
                    brand.setId(rs.getInt("generatedID"));
                }
            }
        }
    }


    private Brand mapRow(ResultSet rs) throws SQLException {
        return new Brand(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description")
        );
    }

    @Override
    public int getBrandIdByName(String name) throws SQLException {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL GetBrandIdByName(?)}")){
            statement.setString(1, name);
            try(ResultSet rs = statement.executeQuery()){
                if(rs.next()){return rs.getInt("id");}
            }
        }
        return 0;
    }
}
