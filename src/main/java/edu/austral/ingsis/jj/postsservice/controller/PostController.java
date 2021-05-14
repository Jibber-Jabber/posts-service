package edu.austral.ingsis.jj.postsservice.controller;

import edu.austral.ingsis.jj.postsservice.dto.PostCreationDto;
import edu.austral.ingsis.jj.postsservice.dto.PostInfoDto;
import edu.austral.ingsis.jj.postsservice.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService){
        this.postService = postService;
    }

    @GetMapping
    public List<PostInfoDto> getAllPosts(){
        return postService.getAllPosts();
    }

    @PostMapping
    public PostInfoDto createPost(@RequestBody @Valid PostCreationDto postCreationDto){
        return postService.createPost(postCreationDto);
    }
}
