package hr.algebra.repositories;

import hr.algebra.models.Use;

import java.sql.SQLException;

public interface UseRepository extends Repository<Use>{
    int GetUseIdByName(String name) throws SQLException;

}
