package hr.algebra.repositories;

import hr.algebra.models.Technology;

import java.sql.SQLException;

public interface TechnologyRepository extends Repository<Technology>{
    int GetTechnologydIdByName(String name) throws SQLException;
}
