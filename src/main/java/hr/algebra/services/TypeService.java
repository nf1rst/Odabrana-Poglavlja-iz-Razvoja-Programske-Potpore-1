package hr.algebra.services;

import hr.algebra.exceptions.CannotPerformActionException;
import hr.algebra.models.Type;
import hr.algebra.repositories.TypeRepository;
import hr.algebra.repositories.factory.RepositoryFactory;
import hr.algebra.util.LoggingUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class TypeService {
    private static final TypeService INSTANCE = new TypeService();
    public static TypeService getInstance(){return INSTANCE;}
    private final TypeRepository repo;

    private TypeService() {
        this.repo= RepositoryFactory.getTypeRespository();
    }

    public List<Type>getAllTypes(){
        try{
            return repo.getAll();
        }
        catch (SQLException _){
            throw new CannotPerformActionException("Cannot execute to get types");
        }
    }

    public void save(Type type){
        try{
            repo.save(type);
        }
        catch (SQLException _){
            throw new CannotPerformActionException("Cannot execute to save type");
        }
    }
    public void delete(int id){
        try {
            repo.delete(id);
        }
        catch (SQLException _){
            throw new CannotPerformActionException("Cannot execute to delete type");
        }
    }

    public List<Type> search(String query){
        if(query==null || query.isEmpty()) return getAllTypes();

        Predicate<Type> containsQuery = t->t.getName().contains(query);

        try{
            return repo.getAll()
                    .stream()
                    .filter(containsQuery)
                    .toList();
        }catch (SQLException _){
            throw new CannotPerformActionException("Cannot execute to get types");
        }
    }

    public boolean nameExists(String name){
        try{
            return repo.getAll()
                    .stream()
                    .anyMatch(t->t.getName().equals(name));
        }
        catch (SQLException _){
            throw new CannotPerformActionException("Cannot excute to get types");
        }

    }

    public int getTypeIdByName(String name){
        try{
            return repo.getTypeIdByName(name);
        }
        catch (SQLException e){
            LoggingUtil.logError("Possible usage insert error", e);
        }
        return 0;
    }

}
