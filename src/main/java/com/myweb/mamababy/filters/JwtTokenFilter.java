package com.myweb.mamababy.filters;

import com.myweb.mamababy.components.JwtTokenUtil;

import com.myweb.mamababy.models.User;
import com.myweb.mamababy.repositories.BlacklistedTokenRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter{
    @Value("${api.prefix}")
    private String apiPrefix;
    private final UserDetailsService userDetailsService;

    @Autowired
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;

    @Override
    protected void doFilterInternal(@NonNull  HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws IOException {
        try {

            if(isBypassToken(request)) {
                filterChain.doFilter(request, response); //enable bypass
                return;
            }
//            final String authHeader = request.getHeader("Authorization");
//            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//                return;
//            }
//            final String token = authHeader.substring(7);
//            final String username = jwtTokenUtil.extractUserName(token);
//            if (username != null
//                    && SecurityContextHolder.getContext().getAuthentication() == null) {
//                User userDetails = (User) userDetailsService.loadUserByUsername(username);
//                if(jwtTokenUtil.validateToken(token, userDetails)) {
//                    UsernamePasswordAuthenticationToken authenticationToken =
//                            new UsernamePasswordAuthenticationToken(
//                                    userDetails,
//                                    null,
//                                    userDetails.getAuthorities()
//                            );
//                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                }
//            }

//             Check if token is blacklisted
//            if (blacklistedTokenRepository.findByToken(token).isPresent()) {
//                logger.warn("JWT Token is blacklisted");
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is blacklisted");
//                return;
//            }

            filterChain.doFilter(request, response); //enable bypass
        }catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }

    }
    private boolean isBypassToken(@NonNull  HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("%s/**", apiPrefix), "GET"),
                Pair.of(String.format("%s/**", apiPrefix), "PUT"),
                Pair.of(String.format("%s/**", apiPrefix), "DELETE"),
                Pair.of(String.format("%s/**", apiPrefix), "POST"),
                Pair.of(String.format("%s/users/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/users/login", apiPrefix), "POST"),
                Pair.of(String.format("%s/stores", apiPrefix), "GET"),
                Pair.of(String.format("%s/products", apiPrefix), "GET"),
                Pair.of(String.format("%s/products/images/**", apiPrefix), "GET"),
                Pair.of(String.format("%s/categories", apiPrefix), "GET"),
                Pair.of(String.format("%s/brands", apiPrefix), "GET"),
                Pair.of(String.format("%s/age", apiPrefix), "GET"),
                Pair.of(String.format("%s/comments", apiPrefix), "GET"),
                Pair.of(String.format("%s/article", apiPrefix), "GET"),
                Pair.of(String.format("%s/vouchers", apiPrefix), "GET")
        );
        for(Pair<String, String> bypassToken: bypassTokens) {
            if (request.getServletPath().contains(bypassToken.getFirst()) &&
                    request.getMethod().equals(bypassToken.getSecond())) {
                return true;
            }
        }
        return false;
    }
}
