package hr.algebra.services;

import hr.algebra.exceptions.CannotPerformActionException;
import hr.algebra.models.Technology;
import hr.algebra.repositories.TechnologyRepository;
import hr.algebra.repositories.implementation.TechnologyRepositoryImpl;
import hr.algebra.util.LoggingUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TechnologyService {
    private final static TechnologyService INSTANCE=new TechnologyService();
    public static TechnologyService getInstance(){return INSTANCE;}
    private final TechnologyRepository repo;

    public TechnologyService() {
        this.repo = TechnologyRepositoryImpl.getInstance();
    }

    public List<Technology>getAllTechnologies(){
        try{
            return repo.getAll();
        }
        catch (SQLException _){
            throw new CannotPerformActionException("Cannot execute to get technologies");
        }
    }
    public Optional<Technology> getTechnologyById(int id){
        try{
            return repo.getById(id);
        }
        catch (SQLException _){
            throw new CannotPerformActionException("Cannot execute to get technology");
        }
    }
    public void save(Technology technology){
        try{
            repo.save(technology);
        }
        catch (SQLException _){
            throw new CannotPerformActionException("Cannot execute to save technology");
        }
    }
    public void delete(int id){
        try{
            repo.delete(id);
        }
        catch (SQLException _){
            throw new CannotPerformActionException("Cannot execute to delete technology");
        }
    }

    public List<Technology>search(String query){
        if(query==null || query.isEmpty())return getAllTechnologies();

        String queryNoCapitals=query.toLowerCase();

        Predicate<Technology>nameMatches= t->t.getName().contains(queryNoCapitals);
        Predicate<Technology>descMatches=t->t.getDescription().contains(queryNoCapitals) && t.getDescription()!=null;


        try{
            return repo.getAll()
                    .stream()
                    .filter(nameMatches.or(descMatches))
                    .sorted()
                    .collect(Collectors.toList());
        }
        catch (SQLException _){
            throw new CannotPerformActionException("Cannot execute to get technolgoies");
        }

    }

    public boolean nameExists(String name){
        try{
            return repo.getAll()
                    .stream()
                    .anyMatch(t->t.getName().equals(name));
        }
        catch (SQLException _){
            throw new CannotPerformActionException("Cannot execute to get technologies");
        }

    }

    public int getTechnologyIdByName(String name){
        try{
            return repo.GetTechnologydIdByName(name);
        }
        catch (SQLException e){
            LoggingUtil.logError("Possible usage insert error", e);
        }
        return 0;
    }

}
