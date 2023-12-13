package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.skypro.homework.component.UserAuth;
import ru.skypro.homework.dto.comment.CommentDto;
import ru.skypro.homework.dto.comment.CommentsDto;
import ru.skypro.homework.dto.comment.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.Announce;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.NotFoundAnnounceException;
import ru.skypro.homework.exception.NotFoundCommentException;
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
     * The method searches for and returns a list of all comments to the ad by the ad id.
     */
    @Override
    public CommentsDto findAllAdComments(Integer adId) {
        announceRepository.checkExistAnnounce(adId).orElseThrow(NotFoundAnnounceException::new);

        List<Comment> listComments = commentRepository.findAllByAd_IdOrderByCreatedAtDesc(adId);
        return commentMapper.commentListToCommentDtoList(listComments);
    }

    /**
     * The method creates a comment to the ad by the ad id.
     */
    @Override
    public CommentDto createComment(Integer id, CreateOrUpdateCommentDto createOrUpdateComment,
                                    UserAuth userDetails) {
        Announce announce = announceRepository.findById(id).orElseThrow(NotFoundAnnounceException::new);
        User currentUSer = userDetails.getUser().orElseThrow(NotFoundUserException::new);
        Comment comment = commentMapper.mapToNewComment(createOrUpdateComment);
        comment.setAd(announce);
        comment.setAuthor(currentUSer);
        return commentMapper.mapToCommentDto(commentRepository.save(comment));
    }

    /**
     * The method deletes the comment to the ad by the ad id and comment id.
     */
    @Override
    @PreAuthorize("hasRole('ADMIN') or @commentServiceImpl.checkAuthor(principal, #commentId)")
    public void deleteAdComment(Integer adId, Integer commentId) {
        announceRepository.checkExistAnnounce(adId).orElseThrow(NotFoundAnnounceException::new);
        if (adId == null || commentId == null) {
            throw new IllegalArgumentException("adId or commentId variables must not be null!");
        }
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);
        commentRepository.delete(comment);
    }

    /**
     * The method update the comment to the ad by the ad id and comment id.
     */
    @Override
    @PreAuthorize("hasRole('ADMIN') or @commentServiceImpl.checkAuthor(principal, #commentId)")
    public CommentDto updateComment(Integer adId, Integer commentId,
                                    CreateOrUpdateCommentDto createOrUpdateCommentDto) {
        announceRepository.checkExistAnnounce(adId).orElseThrow(NotFoundAnnounceException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);
        comment.setText(createOrUpdateCommentDto.getText());
        commentRepository.save(comment);
        return commentMapper.mapToCommentDto(comment);
    }

    /**
     * The method checks the author of the ad.
     */
    public boolean checkAuthor(Principal principal, int commentId) {
        int idUser = userRepository.getIdUserByEmail(principal.getName()).orElseThrow(NotFoundUserException::new);
        return commentRepository.checkAuthorComment(commentId, idUser).orElseThrow(UserNotAuthorCommentException::new);
    }
}
