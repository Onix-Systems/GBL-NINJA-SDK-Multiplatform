package com.gblninja.tag.type;


import com.gblninja.tag.GblType;
import com.gblninja.tag.Tag;
import com.gblninja.tag.TagHeader;
import com.gblninja.tag.TagWithHeader;

public class GblEraseProg implements Tag, TagWithHeader {
    private final TagHeader tagHeader;
    private final GblType tagType;
    private final byte[] tagData;

    public GblEraseProg(TagHeader tagHeader, GblType tagType, byte[] tagData) {
        this.tagHeader = tagHeader;
        this.tagType = tagType;
        this.tagData = tagData;
    }

    @Override
    public GblType getTagType() {
        return tagType;
    }

    @Override
    public Tag copy() {
        return new GblEraseProg(tagHeader, tagType, new byte[0]);
    }

    @Override
    public TagHeader getTagHeader() {
        return tagHeader;
    }

    @Override
    public byte[] getTagData() {
        return tagData;
    }
}