package com.gblninja.tag.type.version;


import com.gblninja.tag.GblType;
import com.gblninja.tag.Tag;

public class GblVersionDependency implements Tag {
    private final GblType tagType;
    private final ImageType imageType;
    private final short statement;
    private final int reversed;
    private final long version;

    public GblVersionDependency(GblType tagType, ImageType imageType, short statement,
                                int reversed, long version) {
        this.tagType = tagType;
        this.imageType = imageType;
        this.statement = statement;
        this.reversed = reversed;
        this.version = version;
    }

    @Override
    public GblType getTagType() {
        return tagType;
    }

    @Override
    public Tag copy() {
        return new GblVersionDependency(tagType, imageType, statement, reversed, version);
    }

    public ImageType getImageType() {
        return imageType;
    }

    public short getStatement() {
        return statement;
    }

    public int getReversed() {
        return reversed;
    }

    public long getVersion() {
        return version;
    }
}