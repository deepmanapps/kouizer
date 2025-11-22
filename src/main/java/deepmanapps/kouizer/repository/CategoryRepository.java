package deepmanapps.kouizer.repository;

import deepmanapps.kouizer.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Find a category by its OpenTriviaDB ID (e.g., to check if it already exists)
    Optional<Category> findByExternalId(Long externalId);
    
    // Find by name for general lookups
    Optional<Category> findByName(String name);
}