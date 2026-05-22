package hr.algebra.repositories.implementation;
import hr.algebra.database.DBConnector;
import hr.algebra.models.*;
import hr.algebra.repositories.BrandRepository;
import hr.algebra.repositories.HeadphonesRepository;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HeadphonesRepositoryImpl implements HeadphonesRepository {
    private static final HeadphonesRepositoryImpl INSTANCE=new HeadphonesRepositoryImpl();
    public static HeadphonesRepositoryImpl getInstace(){return INSTANCE;}
    HeadphonesRepositoryImpl() {}

    @Override
    public List<Headphones> getByBrandID(int brandID) throws SQLException {
        List<Headphones>headphones=new ArrayList<>();
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL GetHeadphonesByBrand(?)}");){
        statement.setInt(1,brandID);
        try(ResultSet rs = statement.executeQuery()){
            while(rs.next())headphones.add(mapRow(rs));
        }
        }
        return headphones;
    }

    private List<Technology> getTechnologiesForHeadphones(int id) throws SQLException {
        List<Technology>technologies=new ArrayList<>();
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL GetTechnologiesForHeadphones(?)}")){
        statement.setInt(1,id);
        try(ResultSet rs = statement.executeQuery()){
            while(rs.next())technologies.add( new Technology(
                    rs.getInt("Id"),
                    rs.getString("Name"),
                    rs.getString("Description")
            ));
        }
        }
        return technologies;
    }

    private List<Use> getPossibleUsagesForHeadphones(int id) throws SQLException {
        List<Use> possibleUsages=new ArrayList<>();
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL GetUsagesForHeadphones(?)}")){
            statement.setInt(1,id);
            try(ResultSet rs = statement.executeQuery()){
                while(rs.next())possibleUsages.add(
                        new Use(
                                rs.getInt("Id"),
                                rs.getString("Name")
                        )
                );
            }
        }
        return possibleUsages;
    }

    @Override
    public List<Headphones> getByTypeID(int typeID) throws SQLException {
        List<Headphones>headphones=new ArrayList<>();
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL GetHeadphonesByType(?)}"))
        {
            statement.setInt(1,typeID);
            try(ResultSet rs = statement.executeQuery()){
                while(rs.next())headphones.add(mapRow(rs));
            }
        }
        return headphones;
    }

    @Override
    public List<Headphones> getByUseID(int useID) throws SQLException {
        List<Headphones>headphones = new ArrayList<>();
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL GetHeadphonesByUse(?)}")){
            statement.setInt(1,useID);
            try(ResultSet rs = statement.executeQuery()){
                while(rs.next())headphones.add(mapRow(rs));
            }
        }
        return headphones;
    }

    @Override
    public List<Headphones> getByMaxPrice(double maxPrice) throws SQLException {
        List<Headphones>headphones = new ArrayList<>();
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL GetHeadphonesByMaxPrice(?)}")){
            statement.setDouble(1,maxPrice);
            try(ResultSet rs = statement.executeQuery()){
                while(rs.next())headphones.add(mapRow(rs));
            }
        }
        return headphones;
    }

    @Override
    public Optional<Headphones> getByName(String name) throws SQLException {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL GetHeadphonesByName(?)}")){
            statement.setString(1, name);
            try(ResultSet rs = statement.executeQuery()){
                if(rs.next())return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public void addTechnology(int headphonesID, int technologyID) throws SQLException {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL AddHeadphonesTechnology(?,?)}")){
            statement.setInt(1,technologyID);
            statement.setInt(2, headphonesID);
            statement.execute();
        }
    }

    @Override
    public void removeTechnology(int headphonesID, int technologyID) throws SQLException {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL RemoveHeadphonesTechnology(?,?)}")){
            statement.setInt(1,technologyID);
            statement.setInt(2, headphonesID);
            statement.execute();
        }
    }

    @Override
    public void addUse(int headphonesID, int useID) throws SQLException {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL AddHeadphonesUse(?,?)}")){
            statement.setInt(1,useID);
            statement.setInt(2, headphonesID);
            statement.execute();
        }
    }


    @Override
    public void removeUse(int headphonesID, int useID) throws SQLException {
        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL RemoveHeadphonesUse(?,?)}")){
            statement.setInt(1,useID);
            statement.setInt(2, headphonesID);
            statement.execute();
        }
    }

    @Override
    public List<Headphones> getAll() throws SQLException {
        List<Headphones>headphones = new ArrayList<>();

        try(CallableStatement statement = DBConnector.getConnection().prepareCall("{CALL GetAllHeadphones()}");
            ResultSet rs = statement.executeQuery();)
        {
            while(rs.next())headphones.add(mapRow(rs));
        }
        return headphones;
    }

    @Override
    public Optional<Headphones> getById(int id) throws SQLException {
        try(CallableStatement statement= DBConnector.getConnection().prepareCall("{CALL GetHeadphonesById(?)}")){
            statement.setInt(1,id);
            try(ResultSet rs = statement.executeQuery()){
                if(rs.next())return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public void delete(int id) throws SQLException {
        try(CallableStatement statement=DBConnector.getConnection().prepareCall("{CALL DeleteHeadphones(?)}")){
            statement.setInt(1,id);
            statement.execute();
        }
    }

    @Override
    public void save(Headphones h) throws SQLException {
        if(h.getId()==0)insert(h);
        else update(h);
    }

    private void update(Headphones h) throws SQLException {
        try(CallableStatement statement=DBConnector.getConnection().prepareCall("{CALL UpdateHeadphones(?,?,?,?,?,?,?,?,?,?,?,?)}")){

            statement.setInt(1,h.getId());
            statement.setString(2,h.getName());
            statement.setInt(3,h.getYearEdition());
            statement.setInt(4,h.getImpedance());
            statement.setInt(5,h.getMinFrequency());
            statement.setInt(6,h.getMaxFrequency());
            statement.setInt(7,h.getBatteryLife());
            statement.setDouble(8,h.getPrice());
            statement.setString(9,h.getDescription());
            statement.setString(10,h.getImagePath());
            statement.setInt(11,h.getBrand().getId());
            statement.setInt(12,h.getType().getId());
            statement.execute();
        }
    }

    private void insert(Headphones h) throws SQLException {
        try(CallableStatement statement=DBConnector.getConnection().prepareCall("{CALL InsertHeadphones(?,?,?,?,?,?,?,?,?,?,?)}")){
            //ovo se moze izdvojit u funkciju da ne moras kopirat dijelove koda
            statement.setString(1,h.getName());
            statement.setInt(2,h.getYearEdition());
            statement.setInt(3,h.getImpedance());
            statement.setInt(4,h.getMinFrequency());
            statement.setInt(5,h.getMaxFrequency());
            statement.setInt(6, h.getBatteryLife());
            statement.setDouble(7,h.getPrice());
            statement.setString(8,h.getDescription());
            statement.setString(9,h.getImagePath());
            statement.setInt(10,h.getBrand().getId());
            statement.setInt(11,h.getType().getId());

            try(ResultSet rs = statement.executeQuery()){
                if(rs.next()){
                    int generatedID = rs.getInt("generatedID");
                    h.setId(generatedID);
                }
            }
        }
    }

    private Headphones mapRow(ResultSet rs) throws SQLException {
    Brand brand = BrandRepositoryImpl.getInstance()
            .getById(rs.getInt("brandID"))
            .orElse(null);

    Type type = TypeRepositoryImpl.getInstance()
            .getById(rs.getInt("TypeID"))
            .orElse(null);

    Headphones headphones = new Headphones(
            rs.getInt("Id"),
            rs.getString("Name"),
            rs.getInt("YearEdition"),
            rs.getInt("Impedance"),
            rs.getInt("MinFrequency"),
            rs.getInt("MaxFrequency"),
            rs.getInt("BatteryLife"),
            rs.getDouble("Price"), // hoce li ovo bit problem jer je price money u db
            rs.getString("Description"),
            rs.getString("ImagePath"),
            brand,
            type,
            new ArrayList<>(),
            new ArrayList<>()
    );

    headphones.setPossibleUsages(getPossibleUsagesForHeadphones(rs.getInt("Id")));
    headphones.setTechnologies(getTechnologiesForHeadphones(rs.getInt("Id")));
    return headphones;
    }
}
