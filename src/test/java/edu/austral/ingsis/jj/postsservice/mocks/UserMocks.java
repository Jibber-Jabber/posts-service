package edu.austral.ingsis.jj.postsservice.mocks;

import edu.austral.ingsis.jj.postsservice.model.UserInfo;

public class UserMocks {
    public static UserInfo getUserInfo() {
        return new UserInfo("123", "test", "test@gmail.com", "test", "test", "ROLE_USER");
    }
}
