package com.qp.qpassessment.security;

import com.qp.qpassessment.utils.AppConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;
    private final AppConfig appConfig;

    @Autowired
    public JwtAuthenticationFilter(final JwtHelper jwtHelper,
                                   final UserDetailsService userDetailsService,
                                   final AppConfig appConfig) {
        this.jwtHelper = jwtHelper;
        this.userDetailsService = userDetailsService;
        this.appConfig = appConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String requestHeader = request.getHeader("Authorization");
        String email = null;
        String token = null;

        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            token = requestHeader.substring(7);
            try {
                email = this.jwtHelper.getEmailFromToken(token);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            log.info(appConfig.getProperty("jwt.token.invalid.header"));
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);

            if (this.jwtHelper.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.info(appConfig.getProperty("jwt.token.validation.fail"));
            }
        }

        filterChain.doFilter(request, response);
    }
}

