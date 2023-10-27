package ru.skypro.homework.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CommentDto {

    @Schema(description = "id автора объявления")
    private long author;

    @Schema(description = "ссылка на аватар автора комментария")
    private String authorImage;

    @Schema(description = "Имя создателя комментария")
    private String authorFirstName;

    @Schema(description = "дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970")
    private Integer createdAt;

    @Schema(description = "id комментария")
    private Integer pk;

    @Schema(description = "текст комментария")
    private String text;

}
