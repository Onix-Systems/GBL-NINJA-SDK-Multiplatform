package com.gblninja.tag;


import java.util.Arrays;

public class DefaultTag implements Tag {
    private final byte[] tagData;
    private final TagHeader tagHeader;
    private final GblType tagType;

    public DefaultTag(byte[] tagData, TagHeader tagHeader, GblType tagType) {
        this.tagData = tagData;
        this.tagHeader = tagHeader;
        this.tagType = tagType;
    }

    @Override
    public GblType getTagType() {
        return tagType;
    }

    @Override
    public Tag copy() {
        return new DefaultTag(new byte[0], tagHeader, tagType);
    }

    public byte[] getTagData() {
        return tagData;
    }

    public TagHeader getTagHeader() {
        return tagHeader;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultTag that = (DefaultTag) o;
        return Arrays.equals(tagData, that.tagData) &&
                tagHeader.equals(that.tagHeader) &&
                tagType == that.tagType;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(tagData);
        result = 31 * result + tagHeader.hashCode();
        result = 31 * result + tagType.hashCode();
        return result;
    }
}