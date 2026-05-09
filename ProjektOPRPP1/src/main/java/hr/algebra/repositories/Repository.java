package hr.algebra.repositories;

import java.util.List;
import java.util.Optional;

public interface Repository <T>{
    List<T> getAll();
    Optional<T> getById(int id); //umjesto nzll- forsira onog koji ga pozove da provjeri jel vrijednost nesto sto nije null
    void delete (int id);
    void save(T object);
}
