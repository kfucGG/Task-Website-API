package ru.kolomiec.taskspring.security.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.kolomiec.taskspring.entity.PersonDetailsSecurityEntity;
import ru.kolomiec.taskspring.services.PersonDetailsService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final PersonDetailsService personDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token != null) {
            UserDetails authenticationPersonDetails = personDetailsService.loadUserByUsername(jwtUtil.getUsernameClaimFromToken(token));
            SecurityContext securityContext = SecurityContextHolder.getContext();
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    authenticationPersonDetails, authenticationPersonDetails.getPassword(), authenticationPersonDetails.getAuthorities()
            );
            if (securityContext.getAuthentication() == null) {
                securityContext.setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
