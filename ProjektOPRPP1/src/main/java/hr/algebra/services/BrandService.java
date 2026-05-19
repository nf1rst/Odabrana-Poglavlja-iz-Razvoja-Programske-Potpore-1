package hr.algebra.services;

import hr.algebra.models.Brand;
import hr.algebra.repositories.BrandRepository;
import hr.algebra.repositories.implementation.BrandRepositoryImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BrandService {
    private final static BrandService INSTANCE=new BrandService();
    private final BrandRepository repo;
    public static BrandService getInstance(){return INSTANCE;}

    public BrandService() {
        repo= BrandRepositoryImpl.getInstance();
    }

    public List<Brand> getAllBrands(){return repo.getAll();}
    public Optional<Brand> getBrandById(int id){return repo.getById(id);}
    public void save(Brand brand) {repo.save(brand);}
    public void delete(int id){repo.delete(id);}

    //za search polje u UI
    public List<Brand> search(String query){
        if(query==null || query.isEmpty())return getAllBrands();

        Predicate<Brand>containsQuery = b->b.getName()
                .toLowerCase()
                .contains(query.toLowerCase().trim());

        //sto je ovo
        return repo.getAll()
                .stream()
                .filter(containsQuery)
                .sorted()
                .toList();
    }


    public boolean nameExists(String name){
        return repo.getAll()
                .stream()
                .anyMatch(b->b.getName().equalsIgnoreCase(name));
    }

}
