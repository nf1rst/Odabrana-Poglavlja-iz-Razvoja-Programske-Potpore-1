package hr.algebra.repositories;

import hr.algebra.models.Technology;

import java.sql.SQLException;

public interface TechnologyRepository extends Repository<Technology>{
    int getTechnologydIdByName(String name) throws SQLException;
}
