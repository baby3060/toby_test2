package com.tobsec.model;

import javax.persistence.*;

public class LevelConverter implements AttributeConverter<Level, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Level level) {
        return level.getValue();
    }

    @Override
    public Level convertToEntityAttribute(Integer value) {
        return Level.valueOf(value);
    }
}