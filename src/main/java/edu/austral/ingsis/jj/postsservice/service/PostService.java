package edu.austral.ingsis.jj.postsservice.service;

import edu.austral.ingsis.jj.postsservice.dto.PostCreationDto;
import edu.austral.ingsis.jj.postsservice.dto.PostInfoDto;
import edu.austral.ingsis.jj.postsservice.model.Post;
import edu.austral.ingsis.jj.postsservice.model.factory.PostFactory;
import edu.austral.ingsis.jj.postsservice.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostFactory postFactory;

    @Autowired
    public PostService(PostRepository postRepository, PostFactory postFactory) {
        this.postRepository = postRepository;
        this.postFactory = postFactory;
    }

    public List<PostInfoDto> getAllPosts(){
        return postRepository.findAll().stream().map(PostInfoDto::from).collect(Collectors.toList());
    }

    public PostInfoDto createPost(PostCreationDto postCreationDto) {
        Post savedPost = postRepository.save(postFactory.convert(postCreationDto));
        return PostInfoDto.from(savedPost);
    }
}
