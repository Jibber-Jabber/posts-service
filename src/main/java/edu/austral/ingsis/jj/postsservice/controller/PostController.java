package edu.austral.ingsis.jj.postsservice.controller;

import edu.austral.ingsis.jj.postsservice.dto.CommentCreationDto;
import edu.austral.ingsis.jj.postsservice.dto.PostCreationDto;
import edu.austral.ingsis.jj.postsservice.dto.PostInfoDto;
import edu.austral.ingsis.jj.postsservice.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
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
    public List<PostInfoDto> homePosts() throws URISyntaxException {
        return postService.getHomePosts();
    }

    @PostMapping
    public PostInfoDto createPost(@RequestBody @Valid PostCreationDto postCreationDto){
        return postService.createPost(postCreationDto);
    }

    @PostMapping("/comments")
    public PostInfoDto addComment(@RequestParam(value = "postId") String postId, @RequestBody @Valid CommentCreationDto commentCreationDto) throws URISyntaxException {
        return postService.addComment(postId, commentCreationDto);
    }
}
