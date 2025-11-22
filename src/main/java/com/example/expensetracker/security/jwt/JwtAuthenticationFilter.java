package com.example.expensetracker.security.jwt;

import com.example.expensetracker.auth.dto.UserContextDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public JwtAuthenticationFilter(JwtService jwtService,
                                   @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtService = jwtService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI().substring(request.getContextPath().length());
        try {
            if (uri.startsWith("/idn")){
                String token = resolveToken(request);

                if (token == null) {
                    // throw UnauthorizedException
                }

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    try {
                        jwtService.validateToken(token, JwtTokenType.ACCESS_TOKEN);
                        Long userId = Long.valueOf(jwtService.extractUserId(token, JwtTokenType.ACCESS_TOKEN));
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(
                                        new UserContextDto(userId),
                                        null,
                                        null
                                );

                        SecurityContextHolder.getContext().setAuthentication(auth);

                    } catch (Exception e) {
                        // throw UnauthorizedException
                    }
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
