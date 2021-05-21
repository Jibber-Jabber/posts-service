package edu.austral.ingsis.jj.postsservice.config;

import edu.austral.ingsis.jj.postsservice.exceptions.BadRequestException;
import edu.austral.ingsis.jj.postsservice.model.UserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.*;
import java.util.*;


public class AuthTokenFilter extends OncePerRequestFilter {

    @Value("${AUTH_HOST}")
    private String authHost;

    @Value("${AUTH_PORT}")
    private String authPort;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, java.io.IOException {
        try {
            if (request.getCookies() == null) throw new BadRequestException("jwt not found");
            Optional<Cookie> jwtCookie = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("jwt")).findFirst();
            if (jwtCookie.isEmpty()) throw new BadRequestException("jwt not found");
            UserInfo userInfo = sendUserServiceRequest(jwtCookie.get().getValue());
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(userInfo.getRole()));

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userInfo, jwtCookie.get().getValue(), authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e){
            response.sendError(401, "Cannot set user authentication: " + e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private UserInfo sendUserServiceRequest(String jwt) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();

        final String getUserUrl = "http://" + authHost + ":" + authPort + "/api/posts/authenticateUser";
        logger.info("Authenticating with: http://" + authHost + ":" + authPort + "/api/posts/authenticateUser");

        URI getUserUri = new URI(getUserUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "jwt="+jwt);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<UserInfo> response = restTemplate.exchange(getUserUri, HttpMethod.GET, httpEntity, UserInfo.class);
        if (response.getStatusCodeValue() != 200) throw new BadRequestException("Authentication Server couldn't authenticate jwt");

        return response.getBody();
    }

}
