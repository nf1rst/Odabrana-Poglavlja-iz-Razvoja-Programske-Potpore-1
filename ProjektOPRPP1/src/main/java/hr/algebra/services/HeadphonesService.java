package hr.algebra.services;
import hr.algebra.models.Brand;
import hr.algebra.models.Headphones;
import hr.algebra.repositories.HeadphonesRepository;
import hr.algebra.repositories.implementation.HeadphonesRepositoryImpl;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class HeadphonesService {
    private final static HeadphonesService INSTANCE=new HeadphonesService();
    public static HeadphonesService getInstance(){return INSTANCE;}
    private final HeadphonesRepository repo;

    public HeadphonesService() {
        this.repo= HeadphonesRepositoryImpl.getInstace();
    }

    public List<Headphones>getAllHeadphones(){return repo.getAll();}
    public Optional<Headphones> getHeadphonesById(int id){return repo.getById(id);}
    public void save(Headphones headphones){repo.save(headphones);}
    public void delete(int id){repo.delete(id);}

    //service sloj se ne brine o tome da to prode kroz bazu to se radi tamo u repo implementaion
    public void addTechnology(int headphonesId, int technologyId){
        repo.addTechnology(headphonesId,technologyId);
    }
    public void removeTechnology(int headphonesId, int technologyId){
        repo.removeTechnology(headphonesId,technologyId);
    }
    public void addUse(int headphonesId, int useId){
        repo.addUse(headphonesId,useId);
    }
    public void removeUse(int headphonesId, int useId){
        repo.removeUse(headphonesId, useId);
    }

    public boolean exists(String name){
        Optional<Headphones> found = repo.getByName(name);
        return found.isPresent();
    }


    public static final Predicate<Headphones>isWireless=h->h.getBatteryLife()>0;
    public static final Predicate<Headphones>isCheap = h->h.getPrice()<100.0;
    public static final Predicate<Headphones>isPreminum=h->h.getPrice()>300.0;

    public List<Headphones>filter(Predicate<Headphones>predicate){
        return repo.getAll()
                .stream()
                .filter(predicate)
                .sorted()
                .collect(Collectors.toList());
    }
    //filteri za UI

    public List<Headphones>getByBrand(int brandId){
        return repo.getByBrandID(brandId);
    }
    public List<Headphones>getByType(int typeId){
        return repo.getByTypeID(typeId);
    }
    public List<Headphones>getByUse(int useId){
        return repo.getByUseID(useId);
    }
    public List<Headphones>getByMaxPrice(double maxPrice){
        return repo.getByMaxPrice(maxPrice);
    }
    //pretraga po brand, name ili desc
    public List<Headphones> search(String query) {
        Predicate<Headphones> nameMatches = h -> h.getName().contains(query);
        Predicate<Headphones> descriptionMatches = h -> h.getDescription() != null && h.getDescription().contains(query);
        Predicate<Headphones> brandMatches = h -> h.getBrand() != null && h.getBrand().getName().contains(query);

        return repo.getAll()
                .stream()
                .filter(nameMatches.or(descriptionMatches).or(brandMatches))
                .sorted()
                .collect(Collectors.toList());
    }

}
