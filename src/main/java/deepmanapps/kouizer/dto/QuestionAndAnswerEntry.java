package deepmanapps.kouizer.dto;

import deepmanapps.kouizer.domain.Answer;
import deepmanapps.kouizer.domain.Question;
import lombok.Data;

import java.util.List;

@Data
public class QuestionAndAnswerEntry {
    Question question;
    List<Answer> answerList;
}
