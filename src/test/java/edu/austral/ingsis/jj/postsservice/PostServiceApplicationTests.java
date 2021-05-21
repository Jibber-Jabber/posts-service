package edu.austral.ingsis.jj.postsservice;

import edu.austral.ingsis.jj.postsservice.dto.PostCreationDto;
import edu.austral.ingsis.jj.postsservice.dto.PostInfoDto;
import edu.austral.ingsis.jj.postsservice.model.Post;
import edu.austral.ingsis.jj.postsservice.repository.PostRepository;
import edu.austral.ingsis.jj.postsservice.service.PostService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PostServiceApplicationTests {

	@Autowired
	private PostService postService;

	@Autowired
	private PostRepository postRepository;

	@Test
	@WithTestUser
	void Should_CreatePost() {
		PostCreationDto postCreation = PostCreationDto.builder()
				.content("test content")
				.build();


		PostInfoDto createdPost = postService.createPost(postCreation);

		Optional<Post> found = postRepository.findById(createdPost.getPostId());
		assertThat(found).isPresent();
		assertThat(found.get().getContent()).isEqualTo("test content");
	}

	@Test
	@WithTestUser
	void Should_ListPosts() {
		PostCreationDto postCreation1 = PostCreationDto.builder()
				.content("test content 1")
				.build();

		PostCreationDto postCreation2 = PostCreationDto.builder()
				.content("test content 2")
				.build();

		postService.createPost(postCreation1);
		postService.createPost(postCreation2);

		List<PostInfoDto> posts = postService.getAllPosts();

		assertThat(posts.size()).isEqualTo(2);
		assertThat(posts.get(0).getContent()).isEqualTo("test content 2");
		assertThat(posts.get(0).getUser().getUsername()).isEqualTo("test");
		assertThat(posts.get(1).getContent()).isEqualTo("test content 1");
		assertThat(posts.get(1).getUser().getUsername()).isEqualTo("test");

	}

}
