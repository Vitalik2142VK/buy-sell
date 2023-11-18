package ru.skypro.homework.mapping;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.comment.CommentDto;
import ru.skypro.homework.dto.comment.CommentsDto;
import ru.skypro.homework.dto.comment.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.Comment;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    /**
     * A method that converts an object of the Comment class to an object of the CommentDto class.
     */
    public CommentDto mapToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setAuthor(comment.getAuthor().getId());
        commentDto.setAuthorImage(comment.getAuthor().getImage());
        commentDto.setAuthorFirstName(comment.getAuthor().getFirstName());
        commentDto.setCreatedAt(comment.getCreatedAt());
        commentDto.setPk(comment.getId());
        commentDto.setText(comment.getText());
        return commentDto;
    }

    /**
     * A method that converts an object of the CommentDto class to an object of the Comment class.
     */
    public Comment mapToNewComment(CreateOrUpdateCommentDto commentDto) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setCreatedAt(System.currentTimeMillis());
        return comment;
    }

    /**
     * A method that converts a collection of the Comment class to a collection of the CommentsDto class.
     */
    public CommentsDto commentListToCommentDtoList(List<Comment> comments) {
        CommentsDto dto = new CommentsDto();
        dto.setCount(comments.size());
        List<CommentDto> commentDtoList = comments
                .stream()
                .map(this::mapToCommentDto).collect(Collectors.toList());
        dto.setResults(commentDtoList);
        return dto;
    }
}
