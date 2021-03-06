package com.dawnfall.caterplanner.user.model.response;

import com.dawnfall.caterplanner.purpose.model.response.ResponsePurpose;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUser {

    private Long id;

    private String name;

    private String profileUrl;

    private String backImageUrl;

    private LocalDateTime joinDate;

    private List<ResponsePurpose> purposeList;

    private Integer successCount;

    private Integer successPer;

    private Boolean isOwner;

}
