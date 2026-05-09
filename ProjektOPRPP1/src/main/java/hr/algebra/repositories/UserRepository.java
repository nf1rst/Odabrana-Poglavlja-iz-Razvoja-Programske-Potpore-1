package hr.algebra.repositories;
import hr.algebra.models.User;
import java.util.Optional;

public interface UserRepository extends Repository<User> {
    Optional<User> getByUsername(String username);
    boolean usernameExists(String username);
}
