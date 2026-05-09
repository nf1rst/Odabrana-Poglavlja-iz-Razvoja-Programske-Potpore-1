package hr.algebra.services;

import hr.algebra.models.Use;
import hr.algebra.repositories.UseRepository;
import hr.algebra.repositories.implementation.UseRepositoryImpl;

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

    public List<Use> getAllUses(){return repo.getAll();}
    public Optional<Use> getUseById(int id){return repo.getById(id);}
    public void save(Use use){repo.save(use);}
    public void delete(int id){repo.delete(id);}

    public List<Use>search(String query){
        if(query==null ||query.trim().isEmpty())return getAllUses();

        Predicate<Use> nameMatches=u->u.getName().contains(query.toLowerCase());

        return repo.getAll()
                .stream()
                .filter(nameMatches)
                .sorted()
                .collect(Collectors.toList());
    }

    public boolean nameExists(String name){
        return repo.getAll()
                .stream()
                .anyMatch(u->u.getName().equals(name));
    }
}
