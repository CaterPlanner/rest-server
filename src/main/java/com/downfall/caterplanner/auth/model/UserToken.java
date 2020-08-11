package com.downfall.caterplanner.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserToken {
    private String token;
    private String refreshToken;
    private long expired;
}
