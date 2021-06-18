package edu.austral.ingsis.jj.postsservice.dto;

import edu.austral.ingsis.jj.postsservice.model.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    @NotNull
    private String username;

    public static UserDto from(UserInfo userInfo) {
        return UserDto.builder()
                .username(userInfo.getUsername())
                .build();
    }
}
