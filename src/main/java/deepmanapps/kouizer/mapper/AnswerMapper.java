package deepmanapps.kouizer.mapper;

import deepmanapps.kouizer.domain.Answer;
import deepmanapps.kouizer.dto.AnswerResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnswerMapper {

    AnswerResponseDTO toAnswerResponseDTO(Answer answer);

    List<AnswerResponseDTO> toAnswerResponseDTOs(List<Answer> answers);
}