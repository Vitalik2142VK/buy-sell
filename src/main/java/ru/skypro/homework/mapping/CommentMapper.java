package ru.skypro.homework.mapping;

import org.springframework.stereotype.Component;
import ru.skypro.homework.component.UserAuth;
import ru.skypro.homework.dto.comment.CommentDto;
import ru.skypro.homework.dto.comment.CommentsDto;
import ru.skypro.homework.dto.comment.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.util.WorkImagePathAndUrl;
import ru.skypro.homework.service.impl.CommentServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {
    private final WorkImagePathAndUrl urlImage;

    public CommentMapper(WorkImagePathAndUrl urlImage) {
        this.urlImage = urlImage;
    }

    /**
     * Converts {@link Comment} into {@link CommentDto}<br>
     * It is used in methods:
     * <br> - {@link CommentServiceImpl#createComment(Integer, CreateOrUpdateCommentDto, UserAuth)}
     * <br> - {@link CommentServiceImpl#updateComment(Integer, Integer, CreateOrUpdateCommentDto)}
     */
    public CommentDto mapToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setAuthor(comment.getAuthor().getId());

        if (comment.getAuthor().getImage() == null || comment.getAuthor().getImage().isEmpty()) {
            commentDto.setAuthorImage(null);
        } else {
            commentDto.setAuthorImage(urlImage.getUserImageUrl(comment.getAuthor().getImage()));
        }
        commentDto.setAuthorFirstName(comment.getAuthor().getFirstName());
        commentDto.setCreatedAt(comment.getCreatedAt());
        commentDto.setPk(comment.getId());
        commentDto.setText(comment.getText());
        return commentDto;
    }

    /**
     * Creates {@link Comment} from {@link CreateOrUpdateCommentDto}<br>
     * It is used in methods:
     * <br> - {@link CommentServiceImpl#createComment(Integer, CreateOrUpdateCommentDto, UserAuth)}
     */
    public Comment mapToNewComment(CreateOrUpdateCommentDto commentDto) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setCreatedAt(System.currentTimeMillis());
        return comment;
    }

    /**
     * Converts {@link List<Comment>} into {@link CommentsDto}<br>
     * It is used in methods:
     * <br> - {@link CommentServiceImpl#findAllAdComments(Integer)}
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
