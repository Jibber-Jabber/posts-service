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
public class PostHomeInfoDto {

    @NotNull
    private String content;

    private String postId;

    private UserDto user;

    private LocalDateTime creationDate;

    private int likeCount;

    private int commentCount;

    private Boolean likedByUser;

    public static PostHomeInfoDto from(Post post, UserInfo userInfo, boolean likedByUser) {
        return PostHomeInfoDto.builder()
                .content(post.getContent())
                .postId(post.getId())
                .user(new UserDto(userInfo.getUsername()))
                .creationDate(post.getCreationDate())
                .likeCount(post.getLikes().size())
                .commentCount(post.getComments().size())
                .likedByUser(likedByUser)
                .build();
    }
}
