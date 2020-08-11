package com.downfall.caterplanner.application.security.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JwtFactory {

    private Algorithm algorithm;

    public String createToken(JwtPayload payload){
        return com.auth0.jwt.JWT.create()
                .withIssuer(String.valueOf(payload.getId()))
                .withExpiresAt(payload.getExpired())
                .sign(algorithm);
    }

}