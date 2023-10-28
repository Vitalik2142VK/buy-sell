package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.announce.Announce;
import ru.skypro.homework.dto.comment.CommentDto;
import ru.skypro.homework.dto.comment.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.Comment;

import java.time.Instant;

@Component
public class CommentMapper {

    public CommentMapper() {
    }

    public CommentDto mapToCommentDto(Comment comment) {
        return new CommentDto(comment);
    }

    public Comment mapToNewComment(CreateOrUpdateCommentDto commentDto) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setCreatedAt(Instant.now().getEpochSecond());
        return comment;
    }
}
