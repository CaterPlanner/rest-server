package com.downfall.caterplanner.application.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.Optional;

@AllArgsConstructor
public class JwtVerifier {

    private Algorithm algorithm;

    public JwtPayload decode(String token) throws JWTVerificationException {
        DecodedJWT jwt = isValidToken(token).orElseThrow(() -> new RuntimeException("인증이 유효하지 않습니다."));

        return JwtPayload.builder()
                    .id(Long.parseLong(jwt.getIssuer()))
                    .expired(jwt.getExpiresAt())
                    .build();
    }

    public Optional<DecodedJWT> isValidToken(String token){
        DecodedJWT jwt = null;

        com.auth0.jwt.JWTVerifier verifier = JWT.require(algorithm).build();
        jwt = verifier.verify(token);

        return Optional.ofNullable(jwt);
    }

}
