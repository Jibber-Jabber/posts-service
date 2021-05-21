package edu.austral.ingsis.jj.postsservice.utils;

import edu.austral.ingsis.jj.postsservice.exceptions.BadRequestException;
import edu.austral.ingsis.jj.postsservice.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class UserUtils {

    @Value("${AUTH_HOST}")
    private String authHost;

    @Value("${AUTH_PORT}")
    private String authPort;

    private static final Logger logger = LoggerFactory.getLogger(UserUtils.class);


    public UserInfo getTokenUserInformation() {
        return (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public UserInfo getUserInfoFromId(String userId) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();


        final String getUserUrl = "http://" + authHost + ":" + authPort + "/api/posts/userInfo";
        logger.info("Authenticating with: http://{}:{}/api/posts/userInfo", authHost, authPort);
        URI getUserUri = new URI(getUserUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "jwt="+ SecurityContextHolder.getContext().getAuthentication().getCredentials().toString());
        headers.add("userId", userId);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<UserInfo> response = restTemplate.exchange(getUserUri, HttpMethod.POST, httpEntity, UserInfo.class);
        if (response.getStatusCodeValue() != 200) throw new BadRequestException("Authentication Server couldn't authenticate jwt");

        return response.getBody();
    }
}
