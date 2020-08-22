package com.downfall.caterplanner.common.entity.enumerate;

import lombok.Getter;

@Getter
public enum StoryType {

    POST(0),
    QNA(1);

    private int value;

    StoryType(int value) {this.value = value;}

    public static StoryType findStoryType(int value){
        for(StoryType storyType : StoryType.values()){
            if(storyType.value == value)
                return storyType;
        }
        return null;
    }
}
