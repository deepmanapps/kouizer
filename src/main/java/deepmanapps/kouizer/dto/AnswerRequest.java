// AnswerRequest.java
package deepmanapps.kouizer.dto;

import lombok.Data;

@Data
public class AnswerRequest {
    private String content;
    private boolean isCorrect;
}