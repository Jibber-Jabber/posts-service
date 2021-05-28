package edu.austral.ingsis.jj.postsservice.repository;

import edu.austral.ingsis.jj.postsservice.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> getAllByUserIdOrderByCreationDate(String userId);

}
