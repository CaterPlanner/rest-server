package com.downfall.caterplanner.purpose.model.response;

import com.downfall.caterplanner.common.entity.Purpose;
import com.downfall.caterplanner.story.model.response.ResponseStory;
import com.downfall.caterplanner.user.model.response.ResponseUser;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
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

    private int disclosureScope;

    private LocalDate startDate;

    private LocalDate endDate;

    private int stat;

    private int achieve;

    private List<ResponseGoal> detailPlans;

    private int cheers;

    private boolean canCheers;

    private List<ResponsePurposeComment> comments;

    private List<ResponseStory> storyTags;

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
