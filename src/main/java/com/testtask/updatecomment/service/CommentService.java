package com.testtask.updatecomment.service;

import com.testtask.updatecomment.model.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {

    List<Comment> fetchComments();
}
