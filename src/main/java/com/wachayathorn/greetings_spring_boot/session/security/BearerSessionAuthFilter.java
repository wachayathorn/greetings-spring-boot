package com.wachayathorn.greetings_spring_boot.session.security;

import java.io.IOException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import tools.jackson.databind.json.JsonMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.wachayathorn.greetings_spring_boot.session.exception.InvalidSessionTokenException;
import com.wachayathorn.greetings_spring_boot.session.service.SessionService;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class BearerSessionAuthFilter extends OncePerRequestFilter {

    private final SessionService sessionService;
    private final JsonMapper jsonMapper;

    public BearerSessionAuthFilter(SessionService sessionService, JsonMapper jsonMapper) {
        this.sessionService = sessionService;
        this.jsonMapper = jsonMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (isApiDocumentation(path)) {
            return true;
        }
        return "POST".equalsIgnoreCase(request.getMethod())
                && ("/api/v1/users".equals(path) || "/api/v1/sessions".equals(path));
    }

    private static boolean isApiDocumentation(String path) {
        return path.startsWith("/swagger-ui")
                || path.equals("/swagger-ui.html")
                || path.startsWith("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            AuthenticatedUser user = sessionService.authenticate(request.getHeader("Authorization"));
            AuthRequestContext.set(user);
            filterChain.doFilter(request, response);
        } catch (InvalidSessionTokenException exception) {
            writeUnauthorized(response);
        } finally {
            AuthRequestContext.clear();
        }
    }

    private void writeUnauthorized(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Authentication required");
        jsonMapper.writeValue(response.getOutputStream(), problem);
    }

}
