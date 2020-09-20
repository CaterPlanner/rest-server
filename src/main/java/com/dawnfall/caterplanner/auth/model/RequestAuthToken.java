package com.dawnfall.caterplanner.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestAuthToken {

    private String idToken;
    private String fcmToken;
}
