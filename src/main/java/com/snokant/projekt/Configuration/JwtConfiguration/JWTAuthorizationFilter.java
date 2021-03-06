package com.snokant.projekt.Configuration.JwtConfiguration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String token = req.getHeader(JwtConstants.HEADER_STRING);


        if (token == null) {
            try {
                chain.doFilter(req, res);
            } catch (JwtException e) {
                System.out.println("HUJ");
            }
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req, res);


        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = request.getHeader(JwtConstants.HEADER_STRING);
        if (token != null) {
            Claims claims = validateToken(token);
            if (claims != null) {
                return new UsernamePasswordAuthenticationToken(claims, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }

    private Claims validateToken(String token) {
        Claims claims = null;
        try {
            return Jwts.parser()
                    .setSigningKey(JwtConstants.SECRET)
                    .parseClaimsJws(token).getBody();
        } catch (Exception e) {
            return null;
        }
    }
}