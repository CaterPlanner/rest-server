package com.downfall.caterplanner.purpose.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponsePurposeComment {

    private Long commentId;

    private Long userId;

    private String userProfileUrl;

    private String userName;

    private String content;

    private LocalDateTime createDate;

}
