package ru.skypro.homework.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CommentsDto {

    @Schema(description = "общее количество комментариев")
    private Integer count;

    @Schema(description = "список всех комментариев")
    private List<CommentDto> results;

}
