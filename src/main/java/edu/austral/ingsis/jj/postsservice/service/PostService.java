package edu.austral.ingsis.jj.postsservice.service;

import edu.austral.ingsis.jj.postsservice.dto.CommentCreationDto;
import edu.austral.ingsis.jj.postsservice.dto.PostCreationDto;
import edu.austral.ingsis.jj.postsservice.dto.PostInfoDto;
import edu.austral.ingsis.jj.postsservice.exceptions.NotFoundException;
import edu.austral.ingsis.jj.postsservice.model.Comment;
import edu.austral.ingsis.jj.postsservice.model.Post;
import edu.austral.ingsis.jj.postsservice.model.UserInfo;
import edu.austral.ingsis.jj.postsservice.model.factory.PostFactory;
import edu.austral.ingsis.jj.postsservice.repository.PostRepository;
import edu.austral.ingsis.jj.postsservice.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
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

    public PostInfoDto createPost(PostCreationDto postCreationDto) {
        UserInfo userInfo = userUtils.getTokenUserInformation();
        Post savedPost = postRepository.save(postFactory.convert(postCreationDto, userInfo));
        return PostInfoDto.from(savedPost, userInfo);
    }

    public List<PostInfoDto> getHomePosts() throws URISyntaxException {
        UserInfo user = userUtils.getTokenUserInformation();
        List<PostInfoDto> ownPosts = postRepository.getAllByUserIdOrderByCreationDate(user.getUserId()).stream().map(post -> PostInfoDto.from(post, user)).collect(Collectors.toList());
        List<UserInfo> followedUsers = userUtils.getFollowedUsersById();
        return Stream.concat(followedUsers.stream().flatMap(userInfo ->
                postRepository.getAllByUserIdOrderByCreationDate(userInfo.getUserId()).stream().map(post -> PostInfoDto.from(post, userInfo))
        ), ownPosts.stream()).sorted(Comparator.comparing(PostInfoDto::getCreationDate).reversed()).collect(Collectors.toList());
    }

    public PostInfoDto addComment(String postId, CommentCreationDto commentCreationDto) throws URISyntaxException {
        UserInfo user = userUtils.getTokenUserInformation();
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found!"));
        List<Comment> comments = post.getComments();
        Comment comment = Comment.builder()
                .content(commentCreationDto.getContent())
                .userId(user.getUserId())
                .build();
        comments.add(comment);
        return PostInfoDto.from(postRepository.save(post), userUtils.getUserInfoFromId(post.getUserId()));
    }
}
