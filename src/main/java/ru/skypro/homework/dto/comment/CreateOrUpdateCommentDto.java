package ru.skypro.homework.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;

public class CreateOrUpdateCommentDto {

    @Schema(description = "текст комментария", minimum = "8", maximum = "64", example = "текст комментария")
    private String text;

}
