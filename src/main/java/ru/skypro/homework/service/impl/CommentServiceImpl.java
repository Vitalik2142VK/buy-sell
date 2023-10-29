package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.comment.Comment;
import ru.skypro.homework.dto.comment.Comments;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
    @Override
    public Comments findAllAdComments(Integer id) {
        return null;
    }

    @Override
    public Comment createComment(Integer id, CreateOrUpdateComment createOrUpdateComment) {
        return null;
    }

    @Override
    public boolean deleteAdComment(Integer adId, Integer commentId) {
        return false;
    }

    @Override
    public Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment createOrUpdateComment) {
        return null;
    }
}
