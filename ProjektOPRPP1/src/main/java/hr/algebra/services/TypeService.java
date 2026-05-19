package hr.algebra.services;
import hr.algebra.models.Type;
import hr.algebra.repositories.TypeRepository;
import hr.algebra.repositories.implementation.TypeRepositoryImpl;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class TypeService {
    private static final TypeService INSTANCE = new TypeService();
    public static TypeService getInstance(){return INSTANCE;}
    private final TypeRepository repo;

    public TypeService() {
        this.repo= TypeRepositoryImpl.getInstance();
    }

    public List<Type>getAllTypes(){return repo.getAll();}
    public Optional<Type> getTypeById(int id){return repo.getById(id);}
    public void save(Type type){repo.save(type);}
    public void delete(int id){repo.delete(id);}

    public List<Type> search(String query){
        if(query==null || query.isEmpty()) return getAllTypes();

        Predicate<Type> containsQuery = t->t.getName().contains(query);

        return repo.getAll()
                .stream()
                .filter(containsQuery)
                .sorted()
                .toList();
    }

    public boolean nameExists(String name){
        return repo.getAll()
                .stream()
                .anyMatch(t->t.getName().equals(name));
    }

}
