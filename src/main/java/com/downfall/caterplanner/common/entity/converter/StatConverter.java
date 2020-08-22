package com.downfall.caterplanner.common.entity.converter;

import com.downfall.caterplanner.common.entity.enumerate.Stat;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StatConverter implements AttributeConverter<Stat, Integer> {


    @Override
    public Integer convertToDatabaseColumn(Stat attribute) {
        return attribute.getValue();
    }

    @Override
    public Stat convertToEntityAttribute(Integer dbData) {
        return Stat.findStat(dbData);
    }
}
