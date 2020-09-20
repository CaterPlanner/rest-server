package com.dawnfall.caterplanner.application.security.token;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
public class PreJwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private String token;

    public PreJwtAuthenticationToken(String token){
        super(token, "");
        this.token = token;
    }

    private PreJwtAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

}
