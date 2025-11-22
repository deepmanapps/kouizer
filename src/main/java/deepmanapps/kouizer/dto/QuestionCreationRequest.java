// QuestionCreationRequest.java
package deepmanapps.kouizer.dto;

import deepmanapps.kouizer.domain.Difficulty;
import deepmanapps.kouizer.domain.QuestionType;
import lombok.Data;
import java.util.List;

@Data
public class QuestionCreationRequest {
    private String content;
    private String categoryName; // The name of the category (e.g., "General Knowledge")
    private Difficulty difficulty;
    private QuestionType type;
    private List<AnswerRequest> answers; 
}