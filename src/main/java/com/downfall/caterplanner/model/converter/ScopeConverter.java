package com.downfall.caterplanner.model.converter;

import com.downfall.caterplanner.model.enumerate.Scope;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ScopeConverter implements AttributeConverter<Scope, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Scope attribute) {
        return attribute.getValue();
    }

    @Override
    public Scope convertToEntityAttribute(Integer dbData) {
        return Scope.findScope(dbData);
    }
}
