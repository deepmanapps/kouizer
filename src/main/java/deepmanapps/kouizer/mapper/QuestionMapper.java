package deepmanapps.kouizer.mapper;

import deepmanapps.kouizer.domain.Question;
import deepmanapps.kouizer.dto.QuestionResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AnswerMapper.class})
public interface QuestionMapper {

    @Mapping(source = "category.name", target = "categoryName") // Map nested field to flat field
    @Mapping(source = "answers", target = "answers") // MapStruct automatically uses AnswerMapper here
    QuestionResponseDTO toQuestionResponseDTO(Question question);
    
    List<QuestionResponseDTO> toQuestionResponseDTOs(List<Question> questions);
}