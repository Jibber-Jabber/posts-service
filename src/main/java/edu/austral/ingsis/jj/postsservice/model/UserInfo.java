package edu.austral.ingsis.jj.postsservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {

    private String userId;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String role;
}
