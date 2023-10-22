package ru.skypro.homework.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CreateOrUpdateComment")
public class CreateOrUpdateComment {

    @Schema(description = "текст комментария", minimum = "8", maximum = "64", example = "текст комментария")
    String text;

}
