package com.gblninja.tag.type.version;

public enum ImageType {
    APPLICATION(0x01L),
    BOOTLOADER(0x02L),
    SE(0x03L);

    private final long value;

    ImageType(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public static ImageType fromValue(long value) {
        for (ImageType type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ImageType value: " + value);
    }
}