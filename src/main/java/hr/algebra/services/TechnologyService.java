package hr.algebra.services;

import hr.algebra.exceptions.CannotPerformActionException;
import hr.algebra.models.Technology;
import hr.algebra.repositories.TechnologyRepository;
import hr.algebra.repositories.factory.RepositoryFactory;
import hr.algebra.util.LoggingUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Predicate;

public class TechnologyService {
    private static final TechnologyService INSTANCE=new TechnologyService();
    public static TechnologyService getInstance(){return INSTANCE;}
    private final TechnologyRepository repo;
    private String sqlError="SQL error";
    private TechnologyService() {
        this.repo = RepositoryFactory.getTechnologyRespository();
    }

    public List<Technology>getAllTechnologies(){
        try{
            return repo.getAll();
        }
        catch (SQLException e){
            LoggingUtil.logError(sqlError, e);
            throw new CannotPerformActionException("Cannot execute to get technologies");
        }
    }

    public void save(Technology technology){
        try{
            repo.save(technology);
        }
        catch (SQLException e){
            LoggingUtil.logError(sqlError, e);
            throw new CannotPerformActionException("Cannot execute to save technology");
        }
    }
    public void delete(int id){
        try{
            repo.delete(id);
        }
        catch (SQLException e){
            LoggingUtil.logError(sqlError, e);
            throw new CannotPerformActionException("Cannot execute to delete technology");
        }
    }

    public List<Technology>search(String query){
        if(query==null || query.isEmpty())return getAllTechnologies();

        Predicate<Technology>nameMatches= t->t.getName().contains(query);
        Predicate<Technology>descMatches=t->t.getDescription().contains(query) && t.getDescription()!=null;


        try{
            return repo.getAll()
                    .stream()
                    .sorted()
                    .filter(nameMatches.or(descMatches))
                    .toList();
        }
        catch (SQLException e){
            LoggingUtil.logError(sqlError, e);
            throw new CannotPerformActionException("Cannot execute to get technolgoies");
        }

    }

    public boolean nameExists(String name){
        try{
            return repo.getAll()
                    .stream()
                    .anyMatch(t->t.getName().equals(name));
        }
        catch (SQLException e){
            LoggingUtil.logError(sqlError, e);
            throw new CannotPerformActionException("Cannot execute to get technologies");
        }

    }

    public int getTechnologyIdByName(String name){
        try{
            return repo.getTechnologydIdByName(name);
        }
        catch (SQLException e){
            LoggingUtil.logError("Possible usage insert error", e);
        }
        return 0;
    }

}
