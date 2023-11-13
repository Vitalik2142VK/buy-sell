package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.comment.CommentDto;
import ru.skypro.homework.dto.comment.CommentsDto;
import ru.skypro.homework.dto.comment.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.Announce;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.exception.NotFoundUserException;
import ru.skypro.homework.exception.UserNotAuthorCommentException;
import ru.skypro.homework.mapping.CommentMapper;
import ru.skypro.homework.repository.AnnounceRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;
    private final AnnounceRepository announceRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    /**
     * The method searches for and returns a list of all comments to the ad by the ad id
     */
    @Override
    public CommentsDto findAllAdComments(Integer adId) {

        LOGGER.info("Was invoked method findAllAdComments by adId = {}", adId);

        if (adId == null) {
            //TODO: исправить на согласованный вариант возврата пустого списка
            return null;
        }
        List<Comment> listComments = commentRepository.findAllByAd_IdOrderByCreatedAtDesc(adId);

        return (CommentsDto) listComments;
    }

    /**
     * The method creates a comment to the ad by the ad id
     */
    @Override
    public CommentDto createComment(Integer id, CreateOrUpdateCommentDto createOrUpdateComment,
                                    UserDetails userDetails) {
        LOGGER.info("Was invoked method createComment by adId = {}", id);
        Announce announce = announceRepository.findById(id).orElseThrow();
        User currentUSer = userRepository.findFirstByEmail(userDetails.getUsername()).orElseThrow(NotFoundUserException::new);
        Comment comment = commentMapper.mapToNewComment(createOrUpdateComment);
        comment.setAd(announce);
        comment.setAuthor(currentUSer);
        Comment saved = commentRepository.save(comment);
        return commentMapper.mapToCommentDto(saved);
    }

    /**
     * The method deletes the comment to the ad by the ad id and comment id
     */
    @Override
    @PreAuthorize("hasRole('ADMIN') or @commentServiceImpl.checkAuthor(principal, #commentId)")
    public boolean deleteAdComment(Integer adId, Integer commentId) {

        LOGGER.info("Was invoked method deleteAdComment by adId = {} and commentId = {}", adId, commentId);
        if (adId == null || commentId == null) {
            throw new IllegalArgumentException("adId or commentId variables must not be null!");
        }
        Announce announce = announceRepository.findById(adId).orElseThrow();
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
//        if (!comment.getAd().equals(announce)) {
//            throw new CommentNotFoundException();
//        }
        commentRepository.delete(comment);
        return true;
    }

    /**
     * The method update the comment to the ad by the ad id and comment id
     */
    @Override
    @PreAuthorize("hasRole('ADMIN') or @commentServiceImpl.checkAuthor(principal, #commentId)")
    public CommentDto updateComment(Integer adId, Integer commentId,
                                    CreateOrUpdateCommentDto createOrUpdateCommentDto) {

        LOGGER.info("Was invoked method updateComment by adId = {} and commentId = {}", adId, commentId);
        if (commentId == null) {
            throw new NullPointerException();
            //TODO добавить логгер
        }

        Announce announce = announceRepository.findById(adId).orElseThrow(); //TODO Заменить на exception отсутствия объявления
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
//        if (!comment.getAd().equals(announce)) {
//            throw new CommentNotFoundException();
//        }
        comment.setText(createOrUpdateCommentDto.getText());
        commentRepository.save(comment);
        return commentMapper.mapToCommentDto(comment);
    }

    /**
     * The method checks the author of the ad
     */
    public boolean checkAuthor(Principal principal, int commentId) {
        User user = userRepository.findFirstByEmail(principal.getName()).orElseThrow(NotFoundUserException::new);
        //TODO добавить и сравнить с комментарием(если query не работает)
        if (!commentRepository.checkAuthorComment(commentId, user.getId())) {
            throw new UserNotAuthorCommentException();
        }
        return true;
    }

}
