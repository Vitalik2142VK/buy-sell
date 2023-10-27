package ru.skypro.homework.service;

import ru.skypro.homework.dto.comment.CommentDto;
import ru.skypro.homework.dto.comment.CommentsDto;
import ru.skypro.homework.dto.comment.CreateOrUpdateCommentDto;

public interface CommentService {

    CommentsDto findAllAdComments(Integer id);

    CommentDto createComment(Integer id, CreateOrUpdateCommentDto createOrUpdateCommentDto);

    boolean deleteAdComment(Integer adId, Integer commentId);

    CommentDto updateComment(Integer adId, Integer commentId, CreateOrUpdateCommentDto createOrUpdateCommentDto);
}
