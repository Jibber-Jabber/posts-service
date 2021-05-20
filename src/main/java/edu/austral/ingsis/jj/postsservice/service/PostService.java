package edu.austral.ingsis.jj.postsservice.service;

import edu.austral.ingsis.jj.postsservice.dto.PostCreationDto;
import edu.austral.ingsis.jj.postsservice.dto.PostInfoDto;
import edu.austral.ingsis.jj.postsservice.exceptions.NotFoundException;
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

    public List<PostInfoDto> getAllPosts(){
        return postRepository.findAll().stream().sorted(Comparator.comparing(Post::getCreationDate).reversed())
                .map(post -> {
                    try {
                        return PostInfoDto.from(post, userUtils.getUserInfoFromId(post.getUserId()));
                    } catch (URISyntaxException e) {
                        throw new NotFoundException("User not found for id: " + post.getUserId());
                    }
                }).collect(Collectors.toList());
    }

    public PostInfoDto createPost(PostCreationDto postCreationDto) {
        UserInfo userInfo = userUtils.getTokenUserInformation();
        Post savedPost = postRepository.save(postFactory.convert(postCreationDto, userInfo));
        return PostInfoDto.from(savedPost, userInfo);
    }
}
