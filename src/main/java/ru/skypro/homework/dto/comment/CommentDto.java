package ru.skypro.homework.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.skypro.homework.entity.Comment;

@Data
@Builder
public class CommentDto {

    @Schema(description = "id автора объявления")
    private long author;

    @Schema(description = "ссылка на аватар автора комментария")
    private String authorImage;

    @Schema(description = "Имя создателя комментария")
    private String authorFirstName;

    @Schema(description = "дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970")
    private Long createdAt;

    @Schema(description = "id комментария")
    private Integer pk;

    @Schema(description = "текст комментария")
    private String text;

    public CommentDto(Comment comment) {
        this.author = comment.getAuthor().getId();
        this.authorImage = comment.getAuthor().getImage();
        this.authorFirstName = comment.getAuthor().getFirstName();
        this.createdAt = comment.getCreatedAt();
        this.pk = comment.getId();
        this.text = comment.getText();
    }

}
