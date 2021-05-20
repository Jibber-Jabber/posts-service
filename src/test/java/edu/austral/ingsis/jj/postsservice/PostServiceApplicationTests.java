package edu.austral.ingsis.jj.postsservice;

import edu.austral.ingsis.jj.postsservice.dto.PostCreationDto;
import edu.austral.ingsis.jj.postsservice.dto.PostInfoDto;
import edu.austral.ingsis.jj.postsservice.model.Post;
import edu.austral.ingsis.jj.postsservice.model.UserInfo;
import edu.austral.ingsis.jj.postsservice.repository.PostRepository;
import edu.austral.ingsis.jj.postsservice.service.PostService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Date;
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
	@WithTestUser(username = "test", password = "test")
	void Should_CreatePost() {
		PostCreationDto postCreation = PostCreationDto.builder()
				.content("test content")
				.build();


		PostInfoDto createdPost = postService.createPost(postCreation);

		Optional<Post> found = postRepository.findById(createdPost.getPostId());
		assertThat(found.isPresent()).isTrue();
		assertThat(found.get().getContent()).isEqualTo("test content");
	}

	@Test
	@WithTestUser(username = "test", password = "test")
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

	private String generateJwtToken(String username, int expiration, String jwtSecret, UserInfo userInfo) {
		String jwt = Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + expiration))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				userInfo, jwt, Collections.singletonList(new SimpleGrantedAuthority(userInfo.getRole())));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		return jwt;
	}

}
