package ru.skypro.homework.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Comment {

    @Schema(description = "id автора объявления")
    Integer author;

    @Schema(description = "ссылка на аватар автора комментария")
    String authorImage;

    @Schema(description = "Имя создателя комментария")
    String authorFirstName;

    @Schema(description = "дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970")
    Integer createdAt;

    @Schema(description = "id комментария")
    Integer pk;

    @Schema(description = "текст комментария")
    String text;

}
