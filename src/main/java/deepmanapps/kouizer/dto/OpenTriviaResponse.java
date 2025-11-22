package deepmanapps.kouizer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class OpenTriviaResponse {
    @JsonProperty("response_code")
    private int responseCode;
    
    @JsonProperty("results")
    private List<OpenTriviaQuestionDTO> results;
}