package edu.austral.ingsis.jj.postsservice.dto;

import edu.austral.ingsis.jj.postsservice.model.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostInfoDto {

    @NotNull
    private String content;

    private UserDto user;

    private LocalDateTime creationDate;

    private int likeCount;

    private int commentCount;

    public static PostInfoDto from(Post post) {
        return PostInfoDto.builder()
                .content(post.getContent())
                .user(new UserDto(post.getUsername()))
                .creationDate(post.getCreationDate())
                .likeCount(0)
                .commentCount(0)
                .build();
    }
}