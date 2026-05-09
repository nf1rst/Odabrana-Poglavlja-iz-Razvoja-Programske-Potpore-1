package hr.algebra.repositories;

import hr.algebra.models.Headphones;
import hr.algebra.models.Technology;

import java.util.List;

public interface HeadphonesRepository extends Repository<Headphones> {
    List<Headphones>getByBrandID(int brandID);
    List<Headphones>getByTypeID(int typeID);
    List<Headphones>getByUseID(int useID);
    List<Headphones>getByMaxPrice(double maxPrice);

    void addTechnology(int headphonesID,int technologyID);
    void removeTechnology(int headphonesID,int technologyID);
    void addUse(int headphonesID, int useID);
    void removeUse(int headphonesID, int useID);
}
