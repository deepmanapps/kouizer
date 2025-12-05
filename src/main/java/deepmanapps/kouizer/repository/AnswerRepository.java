package deepmanapps.kouizer.repository;

import deepmanapps.kouizer.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    
    // Find all answers for a specific question ID
    List<Answer> findByQuestionId(Long questionId);

    @Query(value = "SELECT * FROM answers s WHERE :questionId = s.question_id", nativeQuery = true)
    List<Answer> findAnswersByQuestionId(@Param("questionId") Long questionId);
}