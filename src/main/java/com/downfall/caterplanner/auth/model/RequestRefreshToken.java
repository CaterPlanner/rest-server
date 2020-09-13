package com.downfall.caterplanner.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestRefreshToken {
    private String refreshToken;
}
