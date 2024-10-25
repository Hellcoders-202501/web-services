package com.techcompany.fastporte.users.infrastructure.auth.springsecurity;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SecurityEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        /*Message message = new Message("No autorizado");
        String jsonResponse = objectMapper.writeValueAsString(message);

        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);*/

        //response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autorizado");

    }
}