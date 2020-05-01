package com.leonp967.log.ingesting.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.json.bind.annotation.JsonbCreator;
import java.util.Arrays;

public enum RegionEnum {

    US_EAST("us-east-1", 1),
    US_WEST("us-west-2", 2),
    AP_SOUTH("ap-south-1", 3);

    String description;
    Integer code;

    RegionEnum(String description, Integer code) {
        this.description = description;
        this.code = code;
    }

    public static RegionEnum fromCode(Integer code) {
        return Arrays.stream(values()).filter(region -> region.code.equals(code)).findFirst().orElse(null);
    }

    public String getDescription() {
        return description;
    }
}
