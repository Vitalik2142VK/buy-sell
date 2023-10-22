package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(name = "Comments")
public class Comments {

    @Schema(description = "общее количество комментариев")
    Integer count;

    @Schema(description = "список всех комментариев")
    List<Comment> results;

}
