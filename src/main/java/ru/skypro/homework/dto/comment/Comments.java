package ru.skypro.homework.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skypro.homework.dto.comment.Comment;

import java.util.List;

@Data
public class Comments {

    @Schema(description = "общее количество комментариев")
    Integer count;

    @Schema(description = "список всех комментариев")
    List<Comment> results;

}
