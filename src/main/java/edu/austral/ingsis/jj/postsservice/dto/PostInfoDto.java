package edu.austral.ingsis.jj.postsservice.dto;

import edu.austral.ingsis.jj.postsservice.model.Post;
import edu.austral.ingsis.jj.postsservice.model.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostInfoDto {

    @NotNull
    private String content;

    private String postId;

    private UserDto user;

    private LocalDateTime creationDate;

    private int likeCount;

    private int commentCount;

    public static PostInfoDto from(Post post, UserInfo userInfo) {
        return PostInfoDto.builder()
                .content(post.getContent())
                .postId(post.getId())
                .user(new UserDto(userInfo.getUsername()))
                .creationDate(post.getCreationDate())
                .likeCount(0)
                .commentCount(0)
                .build();
    }
}
