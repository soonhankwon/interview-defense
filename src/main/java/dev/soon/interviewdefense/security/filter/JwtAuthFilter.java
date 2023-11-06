package dev.soon.interviewdefense.security.filter;

import dev.soon.interviewdefense.security.TokenStatus;
import dev.soon.interviewdefense.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService jpaUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        log.info("request URL = {}, method={}", requestURI, request.getMethod());
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authorization == null || !authorization.startsWith("Bearer ")) {
            response.sendError(401);
            return;
        }
        String token = authorization.split(" ")[1];
        TokenStatus tokenStatus = jwtService.validateToken(token);
        if(tokenStatus == TokenStatus.EXPIRED) {
            response.sendError(401);
            return;
        }
        if(StringUtils.hasText(token) && tokenStatus == TokenStatus.VALID) {
            setAuthentication(jwtService.getSubjectFromToken(token));
        }
        filterChain.doFilter(request, response);
    }

    public void setAuthentication(String email) {
        UserDetails userDetails = jpaUserDetailsService.loadUserByUsername(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
