package com.dawnfall.caterplanner.application.security.token;

import com.dawnfall.caterplanner.application.security.jwt.JwtPayload;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
public class PostJwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private JwtPayload payload;

    public PostJwtAuthenticationToken(JwtPayload payload){
        super(payload, payload.getId());
        this.payload = payload;
    }

    private PostJwtAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }
}
