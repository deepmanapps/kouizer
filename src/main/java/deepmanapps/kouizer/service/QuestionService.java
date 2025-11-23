package deepmanapps.kouizer.service;

import deepmanapps.kouizer.domain.*;
import deepmanapps.kouizer.dto.OpenTriviaQuestionDTO;
import deepmanapps.kouizer.dto.OpenTriviaResponse;
import deepmanapps.kouizer.dto.QuestionResponseDTO;
import deepmanapps.kouizer.mapper.QuestionMapper;
import deepmanapps.kouizer.repository.CategoryRepository;
import deepmanapps.kouizer.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;
    private final RestTemplate restTemplate = new RestTemplate(); // Or inject via Bean
    private final QuestionMapper questionMapper;
    private static final String OPEN_TRIVIA_URL = "https://opentdb.com/api.php?amount=";

    /**
     * Main method to get a quiz.
     * Tries to find questions in DB. If not enough, fetches from API, saves them, and returns.
     */
    @Transactional
    public List<QuestionResponseDTO> getQuizQuestions(Long categoryId, int limit) {
        // 1. Try to fetch from local DB first
        List<Question> questions = questionRepository.findRandomQuestionsByCategory(categoryId, limit);

        // 2. If we don't have enough questions, fetch from API, save, and retry
        if (questions.size() < limit) {
            log.info("Not enough questions in DB for category {}. Fetching from OpenTriviaDB...", categoryId);
            fetchAndSaveQuestionsFromApi(categoryId,limit);
            // Fetch again after saving
            questions = questionRepository.findRandomQuestionsByCategory(categoryId, limit);


        }
        
        return questionMapper.toQuestionResponseDTOs(questions);
    }

    @Transactional
    public Question createCustomQuestion(Question question, User user) {
        question.setSource(QuestionSource.USER);
        question.setCreator(user);
        // Ensure answers are linked correctly
        question.getAnswers().forEach(a -> a.setQuestion(question));
        return questionRepository.save(question);
    }

    /**
     * INTERNAL: Fetches from API and maps to Entities
     */
    private void fetchAndSaveQuestionsFromApi(Long localCategoryId,int limit) {
        // Retrieve the external ID (e.g. Local ID 1 maps to External ID 9 "General Knowledge")
        Category category = categoryRepository.findById(localCategoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (category.getExternalId() == null) {
            log.warn("Category {} has no external ID. Cannot fetch from API.", category.getName());
            return;
        }

        String url = OPEN_TRIVIA_URL +limit+ "&category=" + category.getExternalId();
        
        try {
            OpenTriviaResponse response = restTemplate.getForObject(url, OpenTriviaResponse.class);

            if (response != null && response.getResults() != null) {
                List<Question> newQuestions = new ArrayList<>();

                for (OpenTriviaQuestionDTO dto : response.getResults()) {
                    // Map DTO to Entity
                    Question q = new Question();
                    // Decode HTML entities (API returns "CPU &amp; GPU" -> "CPU & GPU")
                    q.setContent(HtmlUtils.htmlUnescape(dto.getQuestion()));
                    q.setDifficulty(Difficulty.valueOf(dto.getDifficulty().toUpperCase()));
                    q.setType(dto.getType().equals("multiple") ? QuestionType.MULTIPLE : QuestionType.BOOLEAN);
                    q.setSource(QuestionSource.API);
                    q.setCategory(category);

                    // Map Correct Answer
                    Answer correct = Answer.builder()
                            .content(HtmlUtils.htmlUnescape(dto.getCorrectAnswer()))
                            .isCorrect(true)
                            .question(q) // Bi-directional link
                            .build();
                    q.getAnswers().add(correct);

                    // Map Incorrect Answers
                    for (String incorrectStr : dto.getIncorrectAnswers()) {
                        Answer incorrect = Answer.builder()
                                .content(HtmlUtils.htmlUnescape(incorrectStr))
                                .isCorrect(false)
                                .question(q)
                                .build();
                        q.getAnswers().add(incorrect);
                    }

                    newQuestions.add(q);
                }
                
                questionRepository.saveAll(newQuestions);
                log.info("Saved {} new questions from API.", newQuestions.size());
            }
        } catch (Exception e) {
            log.error("Error fetching from OpenTriviaDB", e);
        }
    }


    private OpenTriviaResponse fetchQuestionsQueryFromApi(Long localCategoryId,int limit,Difficulty difficulty) {
        // Retrieve the external ID (e.g. Local ID 1 maps to External ID 9 "General Knowledge")
        Category category = categoryRepository.findById(localCategoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (category.getExternalId() == null) {
            log.warn("Category {} has no external ID. Cannot fetch from API.", category.getName());
            return null;
        }

        String url = OPEN_TRIVIA_URL + limit + "&category=" + category.getExternalId() + "&difficulty=" + difficulty;

        OpenTriviaResponse response = null;
        try {
            response = restTemplate.getForObject(url, OpenTriviaResponse.class);

            if (response != null && response.getResults() != null) {
                log.error("Data from OpenTriviaDB is empty");
            }

            log.info("Fetched new questions from API.");
            return response;

        } catch (Exception e) {
            log.error("Error fetching from OpenTriviaDB", e);
            return null;
        }

    }
}