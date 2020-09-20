package com.dawnfall.caterplanner.application.security.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;


@Getter
@AllArgsConstructor
@Builder
public class JwtPayload {
    private Long id;
    private Date expired;
}