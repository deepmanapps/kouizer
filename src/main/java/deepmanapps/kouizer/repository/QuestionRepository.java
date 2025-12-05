package deepmanapps.kouizer.repository;

import deepmanapps.kouizer.domain.Difficulty;
import deepmanapps.kouizer.domain.Question;
import deepmanapps.kouizer.domain.QuestionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    // Fetch all questions in a specific category
    List<Question> findByCategoryId(Long categoryId);

    // Fetch all questions with a specific difficulty
    List<Question> findByDifficulty(Difficulty difficulty);

    // Fetch questions by both category and difficulty (e.g., "Hard History Questions")
    List<Question> findByCategoryIdAndDifficulty(Long categoryId, Difficulty difficulty);
    
    // Custom Query: Get a random set of questions for a quiz
    // Note: "ORDER BY RAND()" is efficient for small datasets but check performance for large ones
    @Query(value = "SELECT * FROM questions q WHERE q.category_id = :categoryId ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Question> findRandomQuestionsByCategory(@Param("categoryId") Long categoryId, @Param("limit") int limit);

}