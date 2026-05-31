package hr.algebra.services;

import hr.algebra.exceptions.CannotPerformActionException;
import hr.algebra.models.Use;
import hr.algebra.repositories.UseRepository;
import hr.algebra.repositories.implementation.UseRepositoryImpl;
import hr.algebra.util.LoggingUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UseService {
    private final static UseService INSTANCE=new UseService();
    public static UseService getInstance(){return INSTANCE;}
    private final UseRepository repo;

    public UseService() {
        this.repo= UseRepositoryImpl.getInstance();
    }

    public List<Use> getAllUses(){
        try{
            return repo.getAll();
        }
        catch (SQLException _){
            throw new CannotPerformActionException("Cannot execute to get uses");
        }
    }
    public Optional<Use> getUseById(int id){
        try{
            return repo.getById(id);
        }
        catch (SQLException _){
            throw new CannotPerformActionException("Cannot execute to get use");
        }
    }
    public void save(Use use){
        try{
            repo.save(use);
        }
        catch (SQLException _){
            throw new CannotPerformActionException("Cannot execute to save use");
        }
    }
    public void delete(int id){
        try{
            repo.delete(id);

        }
        catch (SQLException _){
            throw new CannotPerformActionException("Cannot execute to delete use");
        }
    }

    public List<Use>search(String query){
        if(query==null ||query.trim().isEmpty())return getAllUses();

        Predicate<Use> nameMatches=u->u.getName().contains(query.toLowerCase());

        try{
            return repo.getAll()
                    .stream()
                    .filter(nameMatches)
                    .sorted()
                    .toList();
        }
        catch (SQLException _){
            throw new CannotPerformActionException("Cannot get uses");
        }

    }

    public boolean nameExists(String name){
        try{
            return repo.getAll()
                    .stream()
                    .anyMatch(u->u.getName().equals(name));
        }
        catch (SQLException _){
            throw new CannotPerformActionException("Cannot get uses");
        }
    }

    public int getUseIdByName(String name){
        try{
            return repo.GetUseIdByName(name);
        }
        catch (SQLException e){
            LoggingUtil.logError("Possible usage insert error", e);
        }
        return 0;
    }
}
