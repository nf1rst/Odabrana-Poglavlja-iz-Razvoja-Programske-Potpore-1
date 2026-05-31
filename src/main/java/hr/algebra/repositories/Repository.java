package hr.algebra.repositories;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Repository <T>{
    List<T> getAll() throws SQLException;
    Optional<T> getById(int id) throws SQLException; //umjesto nzll- forsira onog koji ga pozove da provjeri jel vrijednost nesto sto nije null
    void delete (int id) throws SQLException;
    void save(T object) throws SQLException;
}
