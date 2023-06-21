package com.testtask.updatecomment.controller;

import com.testtask.updatecomment.model.Comment;
import com.testtask.updatecomment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserController {

    private final CommentService commentService;

    @Autowired
    public UserController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        List<Comment> comments = commentService.fetchComments();
        model.addAttribute("comments", comments);
        return "user/users";
    }
}
