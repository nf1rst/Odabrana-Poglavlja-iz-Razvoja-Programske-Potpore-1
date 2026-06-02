package hr.algebra.repositories;

import hr.algebra.models.Brand;

import java.sql.SQLException;

public interface BrandRepository extends Repository<Brand>{
    int getBrandIdByName(String name) throws SQLException;
}
