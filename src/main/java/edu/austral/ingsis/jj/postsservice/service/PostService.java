package edu.austral.ingsis.jj.postsservice.service;

import edu.austral.ingsis.jj.postsservice.dto.*;
import edu.austral.ingsis.jj.postsservice.exceptions.NotFoundException;
import edu.austral.ingsis.jj.postsservice.exceptions.UnauthorizedException;
import edu.austral.ingsis.jj.postsservice.model.Comment;
import edu.austral.ingsis.jj.postsservice.model.Post;
import edu.austral.ingsis.jj.postsservice.model.UserInfo;
import edu.austral.ingsis.jj.postsservice.model.factory.PostFactory;
import edu.austral.ingsis.jj.postsservice.repository.PostRepository;
import edu.austral.ingsis.jj.postsservice.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostFactory postFactory;
    private final UserUtils userUtils;

    @Autowired
    public PostService(PostRepository postRepository, PostFactory postFactory, UserUtils userUtils) {
        this.postRepository = postRepository;
        this.postFactory = postFactory;
        this.userUtils = userUtils;
    }

    public PostHomeInfoDto createPost(PostCreationDto postCreationDto) {
        UserInfo userInfo = userUtils.getTokenUserInformation();
        Post savedPost = postRepository.save(postFactory.convert(postCreationDto, userInfo));
        return PostHomeInfoDto.from(savedPost, userInfo, false);
    }

    public void deletePost(String postId) {
        UserInfo userInfo = userUtils.getTokenUserInformation();
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found!"));
        if (!userInfo.getUserId().equals(post.getUserId())) throw new UnauthorizedException("Not post owner!");
        postRepository.deleteById(postId);
    }

    public List<PostHomeInfoDto> getHomePosts() throws URISyntaxException {
        UserInfo user = userUtils.getTokenUserInformation();
        List<PostHomeInfoDto> ownPosts = postRepository.getAllByUserIdOrderByCreationDate(user.getUserId()).stream().map(post ->
                    PostHomeInfoDto.from(post, user, post.getLikes().contains(user.getUserId()))).collect(Collectors.toList());
        List<UserInfo> followedUsers = userUtils.getFollowedUsersById();
        return Stream.concat(followedUsers.stream().flatMap(userInfo ->
                postRepository.getAllByUserIdOrderByCreationDate(userInfo.getUserId()).stream().map(post -> PostHomeInfoDto.from(post, userInfo, post.getLikes().contains(user.getUserId())))
        ), ownPosts.stream()).sorted(Comparator.comparing(PostHomeInfoDto::getCreationDate).reversed()).collect(Collectors.toList());
    }

    public PostInfoDto addComment(String postId, CommentCreationDto commentCreationDto) {
        UserInfo user = userUtils.getTokenUserInformation();
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found!"));
        List<Comment> comments = post.getComments();
        Comment comment = Comment.builder()
                .content(commentCreationDto.getContent())
                .userId(user.getUserId())
                .creationDate(LocalDateTime.now())
                .post(post)
                .build();
        comments.add(comment);
        List<CommentInfoDto> commentDtos = post.getComments().stream().map(commentDto ->
                CommentInfoDto.from(commentDto, userUtils.getUserInfoFromId(commentDto.getUserId()))
        ).collect(Collectors.toList());
        return PostInfoDto.from(postRepository.save(post), userUtils.getUserInfoFromId(post.getUserId()), commentDtos, post.getLikes().contains(user.getUserId()));
    }

    public PostInfoDto getPost(String postId) {
        UserInfo user = userUtils.getTokenUserInformation();
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found!"));

        List<CommentInfoDto> comments = post.getComments().stream().map(comment ->
            CommentInfoDto.from(comment, userUtils.getUserInfoFromId(comment.getUserId()))
        ).collect(Collectors.toList());
        return PostInfoDto.from(post, userUtils.getUserInfoFromId(post.getUserId()), comments, post.getLikes().contains(user.getUserId()));
    }

    public void likePost(String postId){
        UserInfo user = userUtils.getTokenUserInformation();
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found!"));
        post.getLikes().add(user.getUserId());
        postRepository.save(post);
    }

    public void unlikePost(String postId) {
        UserInfo user = userUtils.getTokenUserInformation();
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found!"));
        post.getLikes().remove(user.getUserId());
        postRepository.save(post);
    }

    public List<PostHomeInfoDto> getUserPosts(String userId) {
        UserInfo user = userUtils.getTokenUserInformation();
        List<Post> posts = postRepository.getAllByUserIdOrderByCreationDate(userId);
        return posts.stream().map(post -> PostHomeInfoDto.from(post, userUtils.getUserInfoFromId(userId), post.getLikes().contains(user.getUserId()))).collect(Collectors.toList());
    }
}
