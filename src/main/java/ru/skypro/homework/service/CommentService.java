package ru.skypro.homework.service;

import ru.skypro.homework.dto.comment.Comment;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;

import java.util.List;

public interface CommentService {

    Comments findAllAdComments(Integer id);

    Comment createComment(Integer id, CreateOrUpdateComment createOrUpdateComment);

    boolean deleteAdComment(Integer adId, Integer commentId);

    Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment createOrUpdateComment);
}
