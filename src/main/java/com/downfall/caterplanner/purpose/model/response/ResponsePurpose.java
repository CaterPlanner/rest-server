package com.downfall.caterplanner.purpose.model.response;

import com.downfall.caterplanner.common.entity.Purpose;
import com.downfall.caterplanner.story.model.response.ResponseStory;
import com.downfall.caterplanner.user.model.response.ResponseUser;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    private Integer cheers;

    private Boolean canCheers;

    private LocalDateTime createDate;

    private List<ResponsePurposeComment> comments;

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
