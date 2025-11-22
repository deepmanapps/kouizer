package deepmanapps.kouizer.repository;

import deepmanapps.kouizer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Essential for login/authentication
    Optional<User> findByUsername(String username);
    
    // Useful for registration checks
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}