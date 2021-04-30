package edu.austral.ingsis.jj.postsservice.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PostsService {

    public List<String> getAllPosts(){
        return Arrays.asList("post#1", "post#2");
    }
}
