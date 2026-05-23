package hr.algebra.services;
import hr.algebra.exceptions.CannotPerformActionException;
import hr.algebra.models.Brand;
import hr.algebra.repositories.BrandRepository;
import hr.algebra.repositories.implementation.BrandRepositoryImpl;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class BrandService {
    private static final BrandService INSTANCE=new BrandService();
    private final BrandRepository repo;
    public static BrandService getInstance(){return INSTANCE;}

    public BrandService() {
        repo= BrandRepositoryImpl.getInstance();
    }

    public List<Brand> getAllBrands(){
        try{
            return repo.getAll();
        }
        catch (SQLException _){
            throw new CannotPerformActionException("Cannot execute to get brands");
        }
    }
    public Optional<Brand> getBrandById(int id){
        try{
            return repo.getById(id);
        }
        catch (SQLException _){
            throw new CannotPerformActionException("Cannot execute to get brand");
        }
    }
    public void save(Brand brand) {
        try{
            repo.save(brand);
        }
        catch (SQLException _){
            throw new CannotPerformActionException("Cannot execute to save brand");
        }
    }
    public void delete(int id){
        try{
            repo.delete(id);
        }
        catch (SQLException _){
            throw new CannotPerformActionException("Cannot execute to delete brand");
        }
    }

    //za search polje u UI
    public List<Brand> search(String query){
        if(query==null || query.isEmpty())return getAllBrands();

        Predicate<Brand>containsQuery = b->b.getName()
                .toLowerCase()
                .contains(query.toLowerCase().trim());

        try{
            return repo.getAll()
                    .stream()
                    .filter(containsQuery)
                    .sorted()
                    .toList();
        }
        catch (SQLException _){
            throw new CannotPerformActionException("Cannot filter brands at the moment");
        }

    }


    public boolean nameExists(String name){
        try{
            return repo.getAll()
                    .stream()
                    .anyMatch(b->b.getName().equalsIgnoreCase(name));
        }
        catch (SQLException _){
            throw new CannotPerformActionException("Cannot execute to get brands");
        }

    }

}
