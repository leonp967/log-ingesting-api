package com.leonp967.log.ingesting.model;

import java.util.Arrays;

public enum TimeTypeEnum {

    DAY("D"),
    WEEK("W"),
    YEAR("Y"),
    UNKNOWN("0");

    String code;

    TimeTypeEnum(String code) {
        this.code = code;
    }

    public static TimeTypeEnum fromCode(String code) {
        return Arrays.stream(values()).filter(region ->
                region.code.equalsIgnoreCase(code)).findFirst().orElse(UNKNOWN);
    }
}
