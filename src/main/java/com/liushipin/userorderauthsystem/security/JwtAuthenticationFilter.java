package com.liushipin.userorderauthsystem.security;

import com.liushipin.userorderauthsystem.common.UserContext;
import com.liushipin.userorderauthsystem.service.TokenBlacklistService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final DatabaseUserDetailsService userDetailsService;
    private final TokenBlacklistService tokenBlacklistService;

    public JwtAuthenticationFilter(JwtService jwtService,
                                   DatabaseUserDetailsService userDetailsService,
                                   TokenBlacklistService tokenBlacklistService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String authorization = request.getHeader("Authorization");
            if (authorization != null && authorization.startsWith("Bearer ")) {
                try {
                    authenticate(request, authorization.substring(7));
                } catch (JwtException | UsernameNotFoundException | IllegalArgumentException e) {
                    SecurityContextHolder.clearContext();
                    UserContext.clear();
                }
            }
            filterChain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }

    private void authenticate(HttpServletRequest request, String token) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return;
        }
        if (tokenBlacklistService.isBlacklisted(token)) {
            throw new JwtException("Token is blacklisted");
        }

        String username = jwtService.getUsername(token);
        LoginUser user = (LoginUser) userDetailsService.loadUserByUsername(username);
        Long userId = jwtService.getUserId(token);
        UserContext.setUserId(userId);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
