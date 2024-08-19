package com.cetc.noise.config;

import com.cetc.noise.model.LocalUser;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;


@Component
public class TokenRequestFilter extends OncePerRequestFilter {

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private LocalUser localuser;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = "";
        String password = "";
        Date expri = new Date();

        if (requestTokenHeader != null) {
            Claims claims = tokenUtil.claims(requestTokenHeader);
            username = claims.getSubject();
            password = claims.getId();
            expri = claims.getExpiration();
        } else {
            logger.warn("鉴权过程中 JWT Token 为 null");
        }

        if (username != null && password != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (username.equals(localuser.getLocalusername()) && password.equals(localuser.getLocalpassword())) {
                Date now = new Date();
                if (now.before(expri)){
                    UserDetails userDetails = User.builder()
                            .username(username)
                            .password(password)
                            .authorities("all")
                            .roles("user")
                            .build();
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }else {
                    logger.warn("JWT Token 已过期");
                }
            }else {
                logger.warn("用户名密码错误");
            }
        }
        chain.doFilter(request, response);
    }
}
