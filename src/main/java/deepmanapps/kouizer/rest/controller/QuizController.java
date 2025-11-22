package deepmanapps.kouizer.rest.controller;

import deepmanapps.kouizer.domain.Answer;
import deepmanapps.kouizer.domain.Category;
import deepmanapps.kouizer.domain.Question;
import deepmanapps.kouizer.domain.User;
import deepmanapps.kouizer.dto.QuestionCreationRequest;
import deepmanapps.kouizer.dto.QuestionResponseDTO;
import deepmanapps.kouizer.dto.QuizStartRequest;
import deepmanapps.kouizer.repository.CategoryRepository;
import deepmanapps.kouizer.service.QuestionService;
import deepmanapps.kouizer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QuizController {
    private final QuestionService questionService;
    private final UserService userService;
    private final CategoryRepository categoryRepository; 

    @GetMapping("/quiz/start")
    public ResponseEntity<List<QuestionResponseDTO>> startQuiz(@RequestBody QuizStartRequest request) {
        if (request.getAmount() <= 0 || request.getCategoryId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<QuestionResponseDTO> questions = questionService.getQuizQuestions(
                request.getCategoryId(), 
                request.getAmount());

        // WARNING: In production, you must use a Response DTO here to strip away the 
        // 'isCorrect' flag before sending questions to the client.
        return ResponseEntity.ok(questions);
    }

    /**
     * Endpoint for users to submit a new custom question.
     * URI: POST /api/questions/custom/{userId}
     */
    @PostMapping("/questions/custom/{userId}")
    public ResponseEntity<Question> createCustomQuestion(
            @PathVariable Long userId,
            @RequestBody QuestionCreationRequest request) {

        // 1. Fetch Dependencies (User and Category)
        User creator = userService.getUserById(userId); 
        Category category = categoryRepository.findByName(request.getCategoryName())
                .orElseThrow(() -> new RuntimeException("Category not found: " + request.getCategoryName()));

        // 2. Map DTO to Entity
        Question newQuestion = Question.builder()
                .content(request.getContent())
                .difficulty(request.getDifficulty())
                .type(request.getType())
                .category(category)
                .build();

        // 3. Map Answers and set bi-directional link
        List<Answer> answers = request.getAnswers().stream()
                .map(answerDto -> Answer.builder()
                        .content(answerDto.getContent())
                        .isCorrect(answerDto.isCorrect())
                        .question(newQuestion) // Link Answer back to Question
                        .build())
                .collect(Collectors.toList());

        newQuestion.getAnswers().addAll(answers);

        // 4. Save via Service Layer
        Question savedQuestion = questionService.createCustomQuestion(newQuestion, creator);

        // WARNING: Ensure your Question entity's toString() is not recursive (use @ToString.Exclude on relationships).
        return new ResponseEntity<>(savedQuestion, HttpStatus.CREATED);
    }
}