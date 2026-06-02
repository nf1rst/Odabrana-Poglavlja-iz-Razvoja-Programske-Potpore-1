package hr.algebra.repositories;
import hr.algebra.models.User;

import java.sql.SQLException;
import java.util.Optional;

public interface UserRepository extends Repository<User> {
    Optional<User> getByUsername(String username) throws SQLException;
    boolean usernameExists(String username) throws SQLException;
}
