package edu.austral.ingsis.jj.postsservice.model.factory;

import edu.austral.ingsis.jj.postsservice.dto.PostCreationDto;
import edu.austral.ingsis.jj.postsservice.model.Post;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PostFactory {

    public Post convert(PostCreationDto postCreationDto) {
        Post post = new Post();
        post.setUsername(postCreationDto.getUsername());
        post.setContent(postCreationDto.getContent());
        post.setCreationDate(LocalDateTime.now());
        return post;
    }
}
