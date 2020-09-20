package com.dawnfall.caterplanner.story.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@ToString
public class StoryResource {

    private Long purposeId;

    private String title;

    private String content;

    private int type;

    private int disclosureScope;

}
