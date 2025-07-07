package com.gblninja.tag.type;


import com.gblninja.tag.GblType;
import com.gblninja.tag.Tag;
import com.gblninja.tag.TagHeader;
import com.gblninja.tag.TagWithHeader;

public class GblProg implements Tag, TagWithHeader {
    private final TagHeader tagHeader;
    private final GblType tagType;
    private final long flashStartAddress;
    private final byte[] data;
    private final byte[] tagData;

    public GblProg(TagHeader tagHeader, GblType tagType, long flashStartAddress,
                   byte[] data, byte[] tagData) {
        this.tagHeader = tagHeader;
        this.tagType = tagType;
        this.flashStartAddress = flashStartAddress;
        this.data = data;
        this.tagData = tagData;
    }

    @Override
    public GblType getTagType() {
        return tagType;
    }

    @Override
    public Tag copy() {
        return new GblProg(tagHeader, tagType, flashStartAddress, data, new byte[0]);
    }

    @Override
    public TagHeader getTagHeader() {
        return tagHeader;
    }

    @Override
    public byte[] getTagData() {
        return tagData;
    }

    public long getFlashStartAddress() {
        return flashStartAddress;
    }

    public byte[] getData() {
        return data;
    }
}