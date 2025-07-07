package com.gblninja.tag.type;


import com.gblninja.tag.GblType;
import com.gblninja.tag.Tag;
import com.gblninja.tag.TagHeader;
import com.gblninja.tag.TagWithHeader;

public class GblHeader implements Tag, TagWithHeader {
    private final TagHeader tagHeader;
    private final GblType tagType;
    private final long version;
    private final long gblType;
    private final byte[] tagData;

    public GblHeader(TagHeader tagHeader, GblType tagType, long version, long gblType, byte[] tagData) {
        this.tagHeader = tagHeader;
        this.tagType = tagType;
        this.version = version;
        this.gblType = gblType;
        this.tagData = tagData;
    }

    @Override
    public GblType getTagType() {
        return tagType;
    }

    @Override
    public Tag copy() {
        return new GblHeader(tagHeader, tagType, version, gblType, new byte[0]);
    }

    @Override
    public TagHeader getTagHeader() {
        return tagHeader;
    }

    @Override
    public byte[] getTagData() {
        return tagData;
    }

    public long getVersion() {
        return version;
    }

    public long getGblType() {
        return gblType;
    }
}