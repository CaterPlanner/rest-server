package com.dawnfall.caterplanner.purpose.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PurposeCommentResource {

    private String content;
    private Long purposeId;

}
