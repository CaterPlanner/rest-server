package com.dawnfall.caterplanner.story.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoryCommentResource {

    private String content;
    private Long storyId;

}
