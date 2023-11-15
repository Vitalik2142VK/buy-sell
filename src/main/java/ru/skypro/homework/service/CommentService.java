package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.component.UserAuth;
import ru.skypro.homework.dto.comment.CommentDto;
import ru.skypro.homework.dto.comment.CommentsDto;
import ru.skypro.homework.dto.comment.CreateOrUpdateCommentDto;

public interface CommentService {

    CommentsDto findAllAdComments(Integer adId);

    CommentDto createComment(Integer id,
                             CreateOrUpdateCommentDto createOrUpdateCommentDto,
                             UserAuth userDetails);

    void deleteAdComment(Integer adId, Integer commentId);

    CommentDto updateComment(Integer adId, Integer commentId,
                             CreateOrUpdateCommentDto createOrUpdateCommentDto);
}
