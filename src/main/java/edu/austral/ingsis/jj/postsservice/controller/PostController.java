package edu.austral.ingsis.jj.postsservice.controller;

import edu.austral.ingsis.jj.postsservice.dto.CommentCreationDto;
import edu.austral.ingsis.jj.postsservice.dto.PostCreationDto;
import edu.austral.ingsis.jj.postsservice.dto.PostHomeInfoDto;
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
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<PostHomeInfoDto> homePosts() throws URISyntaxException {
        return postService.getHomePosts();
    }

    @PostMapping
    public PostHomeInfoDto createPost(@RequestBody @Valid PostCreationDto postCreationDto) {
        return postService.createPost(postCreationDto);
    }

    @GetMapping("/{postId}")
    public PostInfoDto getPost(@PathVariable(value = "postId") String postId) {
        return postService.getPost(postId);
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable(value = "postId") String postId) {
        postService.deletePost(postId);
    }

    @GetMapping("/byUser/{userId}")
    public List<PostHomeInfoDto> getUserPosts(@PathVariable(value = "userId") String userId) {
        return postService.getUserPosts(userId);
    }

    @PostMapping("/comments/{postId}")
    public PostInfoDto addComment(@PathVariable(value = "postId") String postId, @RequestBody @Valid CommentCreationDto commentCreationDto) {
        return postService.addComment(postId, commentCreationDto);
    }

    @PostMapping("/like/{postId}")
    public void likePost(@PathVariable(value = "postId") String postId) {
        postService.likePost(postId);
    }

    @PostMapping("/unlike/{postId}")
    public void unLikePost(@PathVariable(value = "postId") String postId) {
        postService.unlikePost(postId);
    }

}
