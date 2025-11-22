package deepmanapps.kouizer.dto;

import deepmanapps.kouizer.domain.Difficulty;
import lombok.Data;

import java.util.List;

// deepmanapps.kouizer.dto/QuestionResponseDTO.java
@Data
public class QuestionResponseDTO {
    private Long id;
    private String content;
    private Difficulty difficulty;
    private String categoryName;
    private List<AnswerResponseDTO> answers;
}