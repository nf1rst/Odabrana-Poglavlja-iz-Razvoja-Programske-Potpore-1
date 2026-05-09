package hr.algebra.services;

import hr.algebra.models.Technology;
import hr.algebra.repositories.TechnologyRepository;
import hr.algebra.repositories.implementation.TechnologyRepositoryImpl;

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

    public List<Technology>getAllTechnologies(){return repo.getAll();}
    public Optional<Technology> getTechnologyById(int id){return repo.getById(id);}
    public void save(Technology technology){repo.save(technology);}
    public void delete(int id){repo.delete(id);}

    public List<Technology>search(String query){
        if(query==null || query.isEmpty())return getAllTechnologies();

        String queryNoCapitals=query.toLowerCase();

        Predicate<Technology>nameMatches= t->t.getName().contains(queryNoCapitals);
        Predicate<Technology>descMatches=t->t.getDescription().contains(queryNoCapitals) && t.getDescription()!=null;


        return repo.getAll()
                .stream()
                .filter(nameMatches.or(descMatches))
                .sorted()
                .collect(Collectors.toList());
    }

    public boolean nameExists(String name){
        return repo.getAll()
                .stream()
                .anyMatch(t->t.getName().equals(name));
    }

}
