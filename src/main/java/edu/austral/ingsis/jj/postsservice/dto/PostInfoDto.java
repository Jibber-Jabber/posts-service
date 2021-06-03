package edu.austral.ingsis.jj.postsservice.dto;

import edu.austral.ingsis.jj.postsservice.model.Post;
import edu.austral.ingsis.jj.postsservice.model.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

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

    private List<CommentInfoDto> comments;

    private Boolean likedByUser;

    public static PostInfoDto from(Post post, UserInfo userInfo, List<CommentInfoDto> comments, boolean likedByUser) {
        return PostInfoDto.builder()
                .content(post.getContent())
                .postId(post.getId())
                .user(new UserDto(userInfo.getUsername()))
                .creationDate(post.getCreationDate())
                .likeCount(post.getLikes().size())
                .comments(comments)
                .likedByUser(likedByUser)
                .build();
    }
}
