package edu.austral.ingsis.jj.postsservice.controller;

import edu.austral.ingsis.jj.postsservice.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("posts")
public class PostsController {

    private final PostsService postsService;

    @Autowired
    public PostsController(PostsService postsService){
        this.postsService = postsService;
    }

    @GetMapping
    public List<String> getAllPosts(){
        return postsService.getAllPosts();
    }
}
