package com.downfall.caterplanner.user.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResource {

    private String name;

    private String profileUrl;

    private String backImageUrl;
}
