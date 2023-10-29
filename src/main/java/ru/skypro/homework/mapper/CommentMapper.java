package ru.skypro.homework.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skypro.homework.converter.CommentToCommentDtoConverter;
import ru.skypro.homework.dto.announce.Announce;
import ru.skypro.homework.dto.comment.CommentDto;
import ru.skypro.homework.dto.comment.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.Comment;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final CommentToCommentDtoConverter toCommentDtoConverter;

    public CommentDto mapToCommentDto(Comment comment) {
        return toCommentDtoConverter.convert(comment);
    }

    public Comment mapToNewComment(CreateOrUpdateCommentDto commentDto) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setCreatedAt(Instant.now().getEpochSecond());
        return comment;
    }
}
