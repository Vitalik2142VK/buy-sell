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

    public CommentDto mapToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto(comment);
        return commentDto;
    }

    public Comment mapToNewComment(CreateOrUpdateCommentDto commentDto) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setCreatedAt(Instant.now().getEpochSecond());
        return comment;
    }

    public CommentsDto CommentListToCommentDtoList(List<Comment> comments) {
        CommentsDto dto = new CommentsDto();
        dto.setCount(comments.size());
        List<CommentDto> commentDtoList = comments
                .stream()
                .map(this :: mapToCommentDto).collect(Collectors.toList());
        dto.setResults(commentDtoList);
        return dto;
    }


}
