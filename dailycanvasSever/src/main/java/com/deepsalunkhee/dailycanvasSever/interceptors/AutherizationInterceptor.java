package com.deepsalunkhee.dailycanvasSever.interceptors;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AutherizationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String authorizationHeader = request.getHeader("Authorization");
        request.setAttribute("tokenStatus", false);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
            return false;
        }

        String accessToken = authorizationHeader.substring(7);

        // Validate the token
        String url = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken;

        if (accessToken == null || accessToken.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Access token is missing or invalid");
            return false;
        }

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Map> validationResponse = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY,
                    Map.class);

            if (validationResponse.getStatusCode() != HttpStatus.OK) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized");
                return false;
            }

            Map<String, Object> responseBody = validationResponse.getBody();
            if (responseBody != null) {
                Object email = responseBody.get("email");
                Object name = responseBody.get("name");

                if (email != null && name != null) {
                    //set emai and name in the headers in the response itself
                    request.setAttribute("email", email.toString());
                    request.setAttribute("name", name.toString());
                    request.setAttribute("tokenStatus", true);
                    
                    return true; // Allow the request to proceed
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("Required user info missing in the response");
                    return false;
                }
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Invalid response from the user info endpoint");
                return false;
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error validating access token: " + e.getMessage());
            return false;
        }

    }
}
