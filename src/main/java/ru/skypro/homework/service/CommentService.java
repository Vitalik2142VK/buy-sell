package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.comment.CommentDto;
import ru.skypro.homework.dto.comment.CommentsDto;
import ru.skypro.homework.dto.comment.CreateOrUpdateCommentDto;

public interface CommentService {

    CommentsDto findAllAdComments(Integer adId);

    CommentDto createComment(Integer id, CreateOrUpdateCommentDto createOrUpdateCommentDto, Authentication authentication);

    boolean deleteAdComment(Integer adId, Integer commentId, Authentication authentication);

    CommentDto updateComment(Integer adId, Integer commentId, CreateOrUpdateCommentDto createOrUpdateCommentDto, Authentication authentication);
}
