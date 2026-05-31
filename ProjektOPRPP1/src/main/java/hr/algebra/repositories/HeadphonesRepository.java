package hr.algebra.repositories;

import hr.algebra.models.Headphones;
import hr.algebra.models.Technology;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface HeadphonesRepository extends Repository<Headphones> {
    List<Headphones>getByBrandID(int brandID) throws SQLException;
    List<Headphones>getByTypeID(int typeID) throws SQLException;
    List<Headphones>getByUseID(int useID) throws SQLException;
    List<Headphones>getByMaxPrice(double maxPrice) throws SQLException;
    Optional<Headphones> getByName(String name) throws SQLException;

    void addTechnology(int headphonesID,int technologyID) throws SQLException;
    void removeTechnology(int headphonesID,int technologyID) throws SQLException;
    void addUse(int headphonesID, int useID) throws SQLException;
    void removeUse(int headphonesID, int useID) throws SQLException;
}
