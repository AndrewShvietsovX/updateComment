package com.testtask.updatecomment.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testtask.updatecomment.model.Comment;
import com.testtask.updatecomment.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public List<Comment> fetchComments() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("https://dummyjson.com/comments", String.class);
        String json = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();

        List<Comment> comments;
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            comments = objectMapper.readValue(jsonNode.get("comments").toString(), new TypeReference<>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        if (comments != null) {
            for (Comment comment : comments) {
                comment.getUser().setUsername(capitalizeUsername(comment.getUser().getUsername()));
                comment.setUpdatedAt(getCurrentDateTime());
                commentRepository.save(comment);
            }
        }
        return comments;
    }

    private String capitalizeUsername(String username) {
        return username.substring(0, 1).toUpperCase() + username.substring(1);
    }

    private String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return now.format(formatter);
    }
}
