package edu.austral.ingsis.jj.postsservice.dto;

import edu.austral.ingsis.jj.postsservice.model.Comment;
import edu.austral.ingsis.jj.postsservice.model.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentInfoDto {

    private String content;

    private UserDto user;

    private LocalDateTime creationDate;

    public static CommentInfoDto from(Comment comment, UserInfo userInfo){
        return CommentInfoDto.builder()
                .content(comment.getContent())
                .creationDate(comment.getCreationDate())
                .user(UserDto.from(userInfo))
                .build();
    }

}
