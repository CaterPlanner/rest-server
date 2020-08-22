package com.downfall.caterplanner.story.model.response;

import com.downfall.caterplanner.purpose.model.response.ResponsePurpose;
import com.downfall.caterplanner.user.model.response.ResponseUser;
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
public class ResponseStory {

    private Long id;

    private String title;

    private String content;

    private int type;

    private ResponsePurpose purpose;

    private ResponseUser profile;

    private int likesCount;

    private int commentCount;

    private boolean canLikes;

    private LocalDateTime createDate;

    private List<ResponseStoryComment> comments;

}
