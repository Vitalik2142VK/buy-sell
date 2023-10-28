package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.converter.CommentToCommentDtoConverter;
import ru.skypro.homework.dto.comment.CommentDto;
import ru.skypro.homework.dto.comment.CommentsDto;
import ru.skypro.homework.dto.comment.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AuthRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AnnounceRepository announceRepository;
    private final AuthRepository authRepository;
    private final CommentMapper commentMapper;
    private final CommentToCommentDtoConverter toCommentDtoConverter;

    @Override
    public CommentsDto findAllAdComments(Integer id) {
        if (id == null) {
            //TODO: исправить на согласованный вариант возврата пустого списка
            return null;
        }
        List<Comment> listComments = commentRepository.findAllByAd_IdOrderByCreatedAtDesc(id);

        return CommentsDto.builder()
                .results(toCommentDtoConverter.convertAll(listComments))
                .count(listComments.size())
                .build();
    }

    @Override
    public CommentDto createComment(Integer id, CreateOrUpdateCommentDto createOrUpdateComment, Authentication authentication) {
        AnnounceEntity announce = announceRepository.findById(id).orElseThrow();
        User currentUSer = authRepository.findFirstByName(authentication.getName()).orElseThrow();
        Comment comment = commentMapper.mapToNewComment(createOrUpdateComment);
        comment.setAd(announce);
        comment.setAuthor(currentUSer);
        Comment saved = commentRepository.save(comment);
        return commentMapper.mapToCommentDto(saved);
    }

    @Override
    public boolean deleteAdComment(Integer adId, Integer commentId, Authentication authentication) {
        return false;
    }

    @Override
    public CommentDto updateComment(Integer adId, Integer commentId, CreateOrUpdateCommentDto createOrUpdateCommentDto, Authentication authentication) {
        AnnounceEntity announce = announceRepository.findById(id).orElseThrow();
        User currentUSer = authRepository.findFirstByName(authentication.getName()).orElseThrow();

        return null;
    }
}
