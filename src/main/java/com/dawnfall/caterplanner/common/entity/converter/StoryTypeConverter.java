package com.dawnfall.caterplanner.common.entity.converter;


import com.dawnfall.caterplanner.common.entity.enumerate.StoryType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StoryTypeConverter  implements AttributeConverter<StoryType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(StoryType attribute) {
        return attribute.getValue();
    }

    @Override
    public StoryType convertToEntityAttribute(Integer dbData) {
        return StoryType.findStoryType(dbData);
    }
}
