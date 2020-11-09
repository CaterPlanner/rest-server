package com.dawnfall.caterplanner.application.security.filter;

import com.dawnfall.caterplanner.application.security.exception.IllegalJwtException;
import com.dawnfall.caterplanner.application.security.token.PreJwtAuthenticationToken;
import com.dawnfall.caterplanner.common.ErrorCode;
import com.dawnfall.caterplanner.common.model.network.ErrorInfo;
import com.dawnfall.caterplanner.common.model.network.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";

    public JwtAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String header = request.getHeader("Authorization");

        if(StringUtils.isEmpty(header) || header.length() < AUTHORIZATION_HEADER_PREFIX.length())
            throw new IllegalJwtException(new ErrorInfo(ErrorCode.NOT_VALID_TOKEN, "유효하지 않은 토큰입니다."));

        String token = header.substring(AUTHORIZATION_HEADER_PREFIX.length());

        return super.getAuthenticationManager().authenticate(new PreJwtAuthenticationToken(token));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);

        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        ErrorInfo errorInfo = ((IllegalJwtException) failed).getErrorInfo();

        response.getWriter().write(
                new ObjectMapper().writeValueAsString(
                        new Response("토큰 인증 실패", new ErrorInfo(errorInfo.getCode(), errorInfo.getMessage()))
                )
        );
    }
}
