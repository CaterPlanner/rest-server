package com.downfall.caterplanner.story.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoryResource {

    private Long purposeId;

    private String title;

    private String content;

    private int type;

}
