package com.downfall.caterplanner.purpose.model.response;

import com.downfall.caterplanner.user.model.response.ResponseUser;
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

    private ResponseUser user;

    private String content;

    private LocalDateTime createDate;

}
