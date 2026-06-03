package hr.algebra.services;

import hr.algebra.exceptions.CannotPerformActionException;
import hr.algebra.models.Headphones;
import hr.algebra.repositories.HeadphonesRepository;
import hr.algebra.repositories.factory.RepositoryFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class HeadphonesService {
    private static final HeadphonesService INSTANCE=new HeadphonesService();
    public static HeadphonesService getInstance(){return INSTANCE;}
    private final HeadphonesRepository repo;

    private HeadphonesService() {
        this.repo= RepositoryFactory.getHeadphonesRespository();
    }

    public List<Headphones>getAllHeadphones(){
        try{
            return repo.getAll();
        }
        catch (SQLException _){
            throw new CannotPerformActionException("Cannot execute to get headphones");
        }
    }
    public Optional<Headphones> getHeadphonesById(int id){
        try{
            return repo.getById(id);
        }
        catch (SQLException _){
            throw new CannotPerformActionException("Cannot execute to get headphones");
        }
    }
    public void save(Headphones headphones){
        try{
            repo.save(headphones);
        }
        catch (SQLException _){
            throw new CannotPerformActionException("Cannot execute to save headphones");
        }
    }
    public void delete(int id){
        try{
            repo.delete(id);
        }
        catch (SQLException _){
            throw new CannotPerformActionException("Cannot execute to delete headphones");
        }
    }

    //service sloj se ne brine o tome da to prode kroz bazu to se radi tamo u repo implementaion
    public void addTechnology(int headphonesId, int technologyId){
        try {
            repo.addTechnology(headphonesId,technologyId);
        } catch (SQLException _) {
            throw new CannotPerformActionException("Cannot execute to add techonolgy to headphones");
        }
    }
    public void removeTechnology(int headphonesId, int technologyId){
        try {
            repo.removeTechnology(headphonesId,technologyId);
        } catch (SQLException _) {
            throw new CannotPerformActionException("Cannot execute to remove techonolgy from headphones");
        }
    }
    public void addUse(int headphonesId, int useId){
        try {
            repo.addUse(headphonesId,useId);
        } catch (SQLException _) {
            throw new CannotPerformActionException("Cannot execute to add use to headphones");
        }
    }
    public void removeUse(int headphonesId, int useId){
        try {
            repo.removeUse(headphonesId, useId);
        } catch (SQLException _) {
            throw new CannotPerformActionException("Cannot execute to remove use from headphones");
        }
    }

    public boolean exists(String name){
        Optional<Headphones> found = Optional.empty();
        try {
            found = repo.getByName(name);
        } catch (SQLException _) {
            throw new CannotPerformActionException("Cannot execute to get headphones");
        }
        return found.isPresent();
    }

    public List<Headphones>filter(Predicate<Headphones>predicate){

        try{
            return repo.getAll()
                    .stream()
                    .filter(predicate)
                    .sorted()
                    .toList();
        }
        catch (SQLException _)
        {
            throw new CannotPerformActionException("Cannot execute to filter headphones");
        }

    }
    //filteri za UI

    public List<Headphones>getByBrand(int brandId){
        try {
            return repo.getByBrandID(brandId);
        } catch (SQLException _) {
            throw new CannotPerformActionException("Cannot execute to get headphones by brand");
        }
    }
    public List<Headphones>getByType(int typeId){
        try {
            return repo.getByTypeID(typeId);
        } catch (SQLException _) {
            throw new CannotPerformActionException("Cannot execute to get headphones by type");
        }
    }
    public List<Headphones>getByUse(int useId){
        try {
            return repo.getByUseID(useId);
        } catch (SQLException _) {
            throw new CannotPerformActionException("Cannot execute to get headphones by use");
        }
    }
    //pretraga po brand, name ili desc
    public List<Headphones> search(String query) {
        Predicate<Headphones> nameMatches = h -> h.getName().contains(query);
        Predicate<Headphones> descriptionMatches = h -> h.getDescription() != null && h.getDescription().contains(query);
        Predicate<Headphones> brandMatches = h -> h.getBrand() != null && h.getBrand().getName().contains(query);

        try{
            return repo.getAll()
                    .stream()
                    .filter(nameMatches.or(descriptionMatches).or(brandMatches))
                    .toList();
        }
        catch (SQLException _){
            throw new CannotPerformActionException("Cannot execute to get headphones");
        }

    }

}
