package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.comment.CommentDto;
import ru.skypro.homework.dto.comment.CommentsDto;
import ru.skypro.homework.dto.comment.CreateOrUpdateCommentDto;
import ru.skypro.homework.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
    @Override
    public CommentsDto findAllAdComments(Integer id) {
        return null;
    }

    @Override
    public CommentDto createComment(Integer id, CreateOrUpdateCommentDto createOrUpdateComment) {
        return null;
    }

    @Override
    public boolean deleteAdComment(Integer adId, Integer commentId) {
        return false;
    }

    @Override
    public CommentDto updateComment(Integer adId, Integer commentId, CreateOrUpdateCommentDto createOrUpdateCommentDto) {
        return null;
    }
}
