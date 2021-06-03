package edu.austral.ingsis.jj.postsservice;

import edu.austral.ingsis.jj.postsservice.dto.CommentCreationDto;
import edu.austral.ingsis.jj.postsservice.dto.PostCreationDto;
import edu.austral.ingsis.jj.postsservice.dto.PostHomeInfoDto;
import edu.austral.ingsis.jj.postsservice.model.Comment;
import edu.austral.ingsis.jj.postsservice.model.Post;
import edu.austral.ingsis.jj.postsservice.repository.PostRepository;
import edu.austral.ingsis.jj.postsservice.service.PostService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URISyntaxException;
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
	@WithTestUser(username = "postCreationUser", userId = "1")
	void Should_CreatePost() {
		PostCreationDto postCreation = PostCreationDto.builder()
				.content("test content")
				.build();


		PostHomeInfoDto createdPost = postService.createPost(postCreation);

		Optional<Post> found = postRepository.findById(createdPost.getPostId());
		assertThat(found).isPresent();
		assertThat(found.get().getContent()).isEqualTo("test content");
		assertThat(found.get().getUserId()).isEqualTo("1");
	}

	@Test
	@WithTestUser
	void Should_ListPosts() throws URISyntaxException {
		PostCreationDto postCreation1 = PostCreationDto.builder()
				.content("test content 1")
				.build();

		PostCreationDto postCreation2 = PostCreationDto.builder()
				.content("test content 2")
				.build();

		postService.createPost(postCreation1);
		postService.createPost(postCreation2);

		List<PostHomeInfoDto> posts = postService.getHomePosts();

		assertThat(posts.size()).isEqualTo(2);
		assertThat(posts.get(0).getContent()).isEqualTo("test content 2");
		assertThat(posts.get(0).getUser().getUsername()).isEqualTo("test");
		assertThat(posts.get(1).getContent()).isEqualTo("test content 1");
		assertThat(posts.get(1).getUser().getUsername()).isEqualTo("test");
	}

	@Test
	@WithTestUser
	void Should_likeAndUnlikePost() {
		PostCreationDto postCreation1 = PostCreationDto.builder()
				.content("test content 1")
				.build();
		PostHomeInfoDto postDto = postService.createPost(postCreation1);

		postService.likePost(postDto.getPostId());
		Optional<Post> post = postRepository.findById(postDto.getPostId());
		assertThat(post.get().getLikes()).containsOnly("123"); // "test" userId

		postService.unlikePost(postDto.getPostId());
		post = postRepository.findById(postDto.getPostId());
		assertThat(post.get().getLikes()).isEmpty();
	}

	@Test
	@WithTestUser
	void Should_CommentPost() {
		PostCreationDto postCreation1 = PostCreationDto.builder()
				.content("test content 1")
				.build();
		PostHomeInfoDto postDto = postService.createPost(postCreation1);

		CommentCreationDto commentCreationDto = CommentCreationDto.builder()
				.content("test comment")
				.build();

		postService.addComment(postDto.getPostId(), commentCreationDto);
		Optional<Post> post = postRepository.findById(postDto.getPostId());
		Comment comment = post.get().getComments().get(0);
		assertThat(comment.getContent()).isEqualTo("test comment");
		assertThat(comment.getUserId()).isEqualTo("123");
	}

}
