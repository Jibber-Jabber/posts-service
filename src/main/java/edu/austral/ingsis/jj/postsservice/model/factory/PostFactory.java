package edu.austral.ingsis.jj.postsservice.model.factory;

import edu.austral.ingsis.jj.postsservice.dto.PostCreationDto;
import edu.austral.ingsis.jj.postsservice.model.Post;
import edu.austral.ingsis.jj.postsservice.model.UserInfo;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

@Component
public class PostFactory {

    public Post convert(PostCreationDto postCreationDto, UserInfo userInfo) {
        Post post = new Post();
        post.setUserId(userInfo.getUserId());
        post.setContent(postCreationDto.getContent());
        post.setCreationDate(LocalDateTime.now());
        post.setLikes(Collections.emptySet());
        post.setComments(new ArrayList<>());
        return post;
    }
}
