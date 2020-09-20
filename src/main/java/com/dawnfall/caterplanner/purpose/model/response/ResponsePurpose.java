package com.dawnfall.caterplanner.purpose.model.response;

import com.dawnfall.caterplanner.common.entity.Purpose;
import com.dawnfall.caterplanner.story.model.response.ResponseStory;
import com.dawnfall.caterplanner.user.model.response.ResponseUser;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // 안해주면 null이 "null"로 전달됨
public class ResponsePurpose {

    private Long id;

    private ResponseUser author;

    private String name;

    private String description;

    private String photoUrl;

    private Integer disclosureScope;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer stat;

    private Integer achieve;

    private List<ResponseGoal> detailPlans;

    private Integer cheersCount;

    private Boolean canCheer;

    private LocalDateTime createDate;

    private List<ResponsePurposeComment> comments;

    private Integer commentCount;

    private List<ResponseStory> storyTags;

    private Boolean isOwner;

    public static ResponsePurposeBuilder defaultBuilder(Purpose purpose){
        return ResponsePurpose.builder()
                .id(purpose.getId())
                .name(purpose.getName())
                .description(purpose.getDescription())
                .photoUrl(purpose.getPhotoUrl())
                .disclosureScope(purpose.getDisclosureScope().getValue())
                .startDate(purpose.getStartDate())
                .endDate(purpose.getEndDate())
                .stat(purpose.getStat().getValue());
    }

}
