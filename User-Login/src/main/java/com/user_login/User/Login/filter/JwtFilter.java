package com.user_login.User.Login.filter;

import com.user_login.User.Login.service.AppUserServiceDetails;
import com.user_login.User.Login.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AppUserServiceDetails appUserServiceDetails;
    private static final List<String> PUBLIC_URLS=List.of("/login","/register","/reset-password","/logout");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String path=request.getServletPath();
    if(PUBLIC_URLS.contains(path)){
        filterChain.doFilter(request, response);
        return;
    }

    String jwtToken=null;
    String email=null;

    final String authorizationHeader=request.getHeader("Authorization");
    if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")){
        jwtToken=authorizationHeader.substring(7);
    }
    if (jwtToken==null){
        Cookie[] cookies=request.getCookies();
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    jwtToken=cookie.getValue();
                }
            }
        }
    }

    if (jwtToken!=null){
        email=jwtUtil.getUsernameByToken(jwtToken);
        if(email!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails =appUserServiceDetails.loadUserByUsername(email);
            if(jwtUtil.validateToken(jwtToken,userDetails)){
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }


        }
    }

        filterChain.doFilter(request, response);
    }
}
