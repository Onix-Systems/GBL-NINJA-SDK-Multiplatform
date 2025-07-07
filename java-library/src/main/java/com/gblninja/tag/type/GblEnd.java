package com.gblninja.tag.type;

import com.gblninja.tag.GblType;
import com.gblninja.tag.Tag;
import com.gblninja.tag.TagHeader;
import com.gblninja.tag.TagWithHeader;

import java.util.Arrays;

public class GblEnd implements Tag, TagWithHeader {
    private final TagHeader tagHeader;
    private final GblType tagType;
    private final long gblCrc;
    private final byte[] tagData;

    public GblEnd(TagHeader tagHeader, GblType tagType, long gblCrc, byte[] tagData) {
        this.tagHeader = tagHeader;
        this.tagType = tagType;
        this.gblCrc = gblCrc;
        this.tagData = tagData;
    }

    @Override
    public GblType getTagType() {
        return tagType;
    }

    @Override
    public Tag copy() {
        return new GblEnd(tagHeader, tagType, gblCrc, new byte[0]);
    }

    @Override
    public TagHeader getTagHeader() {
        return tagHeader;
    }

    @Override
    public byte[] getTagData() {
        return tagData;
    }

    public long getGblCrc() {
        return gblCrc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GblEnd gblEnd = (GblEnd) o;
        return gblCrc == gblEnd.gblCrc &&
                tagHeader.equals(gblEnd.tagHeader) &&
                tagType == gblEnd.tagType &&
                Arrays.equals(tagData, gblEnd.tagData);
    }

    @Override
    public int hashCode() {
        int result = tagHeader.hashCode();
        result = 31 * result + tagType.hashCode();
        result = 31 * result + Long.hashCode(gblCrc);
        result = 31 * result + Arrays.hashCode(tagData);
        return result;
    }
}