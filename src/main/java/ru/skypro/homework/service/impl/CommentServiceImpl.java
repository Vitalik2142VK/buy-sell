package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.comment.CommentDto;
import ru.skypro.homework.dto.comment.CommentsDto;
import ru.skypro.homework.dto.comment.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.Announce;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapping.CommentMapper;
import ru.skypro.homework.repository.AnnounceRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AnnounceRepository announceRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentsDto findAllAdComments(Integer adId) {

        if (adId == null) {
            //TODO: исправить на согласованный вариант возврата пустого списка
            return null;
        }
        List<Comment> listComments = commentRepository.findAllByAd_IdOrderByCreatedAtDesc(adId);

//        return CommentsDto.builder()
//                .results(toCommentDtoConverter.convertAll(listComments))
//                .count(listComments.size())
//                .build();
        return (CommentsDto) listComments;
    }

    @Override
    public CommentDto createComment(Integer id, CreateOrUpdateCommentDto createOrUpdateComment,
                                    Authentication authentication) {

        Announce announce = announceRepository.findById(Long.valueOf(id)).orElseThrow();
        User currentUSer = userRepository.findFirstByName(authentication.getName()).orElseThrow();
        Comment comment = commentMapper.mapToNewComment(createOrUpdateComment);
        comment.setAd(announce);
        comment.setAuthor(currentUSer);
        Comment saved = commentRepository.save(comment);
        return commentMapper.mapToCommentDto(saved);

    }

    @Override
    public boolean deleteAdComment(Integer adId, Integer commentId,
                                   Authentication authentication) {
        if (adId == null || commentId == null) {
            throw new IllegalArgumentException("adId or commentId variables must not be null!");
        }
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        User currentUSer = userRepository.findFirstByName(authentication.getName()).orElseThrow();
        Announce announce = announceRepository.findById(Long.valueOf(adId)).orElseThrow();

        if (!comment.getAd().equals(announce)) {
            throw new IllegalArgumentException("not found");
        }
        if (!comment.getAuthor().equals(currentUSer)) {
            throw new IllegalArgumentException("not permit");
        }
        commentRepository.delete(comment);
        return true;

    }

    @Override
    public CommentDto updateComment(Integer adId, Integer commentId,
                                    CreateOrUpdateCommentDto createOrUpdateCommentDto,
                                    Authentication authentication) {
        if (commentId == null) {
            return null;
        }
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        User currentUSer = userRepository.findFirstByName(authentication.getName()).orElseThrow();
        Announce announce = announceRepository.findById(Long.valueOf(adId)).orElseThrow();
        if (!comment.getAd().equals(announce)) {
            throw new IllegalArgumentException("not found");
        } else if (!comment.getAuthor().equals(currentUSer)) {
            throw new IllegalArgumentException("not permit");
        }
        comment.setText(createOrUpdateCommentDto.getText());
        commentRepository.save(comment);
        return commentMapper.mapToCommentDto(comment);

    }


}
