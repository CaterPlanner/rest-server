package com.dawnfall.caterplanner.story.model.response;

import com.dawnfall.caterplanner.purpose.model.response.ResponsePurpose;
import com.dawnfall.caterplanner.user.model.response.ResponseUser;
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

    private Integer type;

    private ResponsePurpose purpose;

    private ResponseUser author;

    private Integer likesCount;

    private Integer commentCount;

    private Boolean canLikes;

    private Boolean isOwner;

    private LocalDateTime createDate;

    private List<ResponseStoryComment> comments;

}
