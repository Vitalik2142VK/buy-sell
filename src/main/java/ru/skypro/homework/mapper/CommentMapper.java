package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.comment.CommentDto;
import ru.skypro.homework.entity.CommentEntity;

@Component
public class CommentMapper {
    private final UserRepository userRepository;
    private final AnnounceRepository announceRepository;

    public CommentMapper(UserRepository userRepository,
                         AnnounceRepository announceRepository) {
        this.userRepository = userRepository;
        this.announceRepository = announceRepository;
    }

    public CommentDto mapToCommentDto(CommentEntity commentEntity) {
        CommentDto dto = new CommentDto();
        dto.setAuthor(commentEntity.getAuthor().getId());
        dto.setAuthorImage(commentEntity.getAuthor().getImage());
        dto.setAuthorFirstName(commentEntity.getAuthor().getFirstName());
        dto.setCreatedAt((int) commentEntity.getCreatedAt());
        dto.setPk(commentEntity.getId());
        dto.setText(commentEntity.getText());
        return dto;
    }

    public CommentEntity mapToCommentEntity(CommentDto commentDto) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setAuthor(commentDto.getAuthor().getFirstName);
        commentEntity.setText(commentDto.getText());
        commentEntity.setCreatedAt(commentDto.getCreatedAt());
    }

}
