package com.deepsalunkhee.dailycanvasSever.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
@Slf4j
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info(" Interceptor: Validating token");

        String authorizationHeader = request.getHeader("Authorization");
        String refreshToken = request.getHeader("refreshToken");
        request.setAttribute("tokenStatus", false);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            respondWithError(response, HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return false;
        }

        String accessToken = authorizationHeader.substring(7);

        if (accessToken.isEmpty()) {
            respondWithError(response, HttpServletResponse.SC_BAD_REQUEST, "Access token is empty");
            return false;
        }

        // Try initial validation
        Map<String, Object> userInfo = getUserInfo(accessToken);

        // If initial token is invalid, try refreshing
        if (userInfo == null && refreshToken != null && !refreshToken.isEmpty()) {
            accessToken = refreshAccessToken(refreshToken);
            if (accessToken != null) {
                userInfo = getUserInfo(accessToken);
            }
        }

        if (userInfo == null) {
            respondWithError(response, HttpServletResponse.SC_UNAUTHORIZED, "Access token invalid or refresh failed");
            return false;
        }

        // Set attributes from user info
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");

        if (email == null || name == null) {
            respondWithError(response, HttpServletResponse.SC_BAD_REQUEST, "User info incomplete in response");
            return false;
        }

        request.setAttribute("accessToken", accessToken);
        request.setAttribute("email", email);
        request.setAttribute("name", name);
        request.setAttribute("tokenStatus", true);

        return true;
    }

    private Map<String, Object> getUserInfo(String accessToken) {
        try {
            String url = UriComponentsBuilder.fromUriString(USER_INFO_URL)
                    .queryParam("access_token", accessToken)
                    .toUriString();

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, Map.class);

            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.warn(" Failed to fetch user info: {}", e.getResponseBodyAsString());
            return null;
        }
    }

    private String refreshAccessToken(String refreshToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String body = "client_id=" + clientId +
                          "&client_secret=" + clientSecret +
                          "&grant_type=refresh_token" +
                          "&refresh_token=" + refreshToken;

            HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    TOKEN_URL,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("access_token")) {
                return (String) responseBody.get("access_token");
            }

            log.warn(" Refresh token response invalid: {}", responseBody);
        } catch (Exception e) {
            log.error(" Error refreshing token: {}", e.getMessage(), e);
        }

        return null;
    }

    private void respondWithError(HttpServletResponse response, int statusCode, String message) throws Exception {
        response.setStatus(statusCode);
        response.setContentType("text/plain");
        response.getWriter().write(message);
    }
}
