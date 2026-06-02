package hr.algebra.repositories;

import hr.algebra.models.Type;

import java.sql.SQLException;

public interface TypeRepository extends Repository<Type>{
    int getTypeIdByName(String name) throws SQLException;

}
