package ru.skypro.homework.converter;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.comment.CommentDto;
import ru.skypro.homework.dto.comment.CommentsDto;
import ru.skypro.homework.entity.Comment;

@Component
public class CommentToCommentDtoConverter extends AbstractConverter<Comment, CommentDto> {
    @Override
    public CommentDto apply(Comment comment) {
        return CommentDto.builder()
                .author(comment.getAuthor().getId())
                .authorFirstName(comment.getAuthor().getFirstName())
                .authorImage(comment.getAuthor().getImage())
                .text(comment.getText())
                .createdAt(comment.getCreatedAt())
                .pk(comment.getId())
                .build();
    }

}
