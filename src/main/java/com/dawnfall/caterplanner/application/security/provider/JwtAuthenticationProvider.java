package com.dawnfall.caterplanner.application.security.provider;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.dawnfall.caterplanner.application.security.exception.IllegalJwtException;
import com.dawnfall.caterplanner.application.security.jwt.JwtPayload;
import com.dawnfall.caterplanner.application.security.jwt.JwtVerifier;
import com.dawnfall.caterplanner.application.security.token.PostJwtAuthenticationToken;
import com.dawnfall.caterplanner.application.security.token.PreJwtAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private JwtVerifier jwtVerifier;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            JwtPayload jwtPayload = jwtVerifier.decode(((PreJwtAuthenticationToken) authentication).getToken());

            return new PostJwtAuthenticationToken(jwtPayload);
        }catch (TokenExpiredException e){
            e.printStackTrace();
            throw new IllegalJwtException("기간이 지난 토큰입니다.");
        }catch (Exception e){
            e.printStackTrace();
            throw  new IllegalJwtException("유효하지 않은 토큰입니다.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PreJwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
