package deepmanapps.kouizer.dto;

import lombok.Data;

@Data
public class QuizStartRequest {
    private Long categoryId;
    private int amount;
    private String difficulty;// Number of questions requested (e.g., 10)
}