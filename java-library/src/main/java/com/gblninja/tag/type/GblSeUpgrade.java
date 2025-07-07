package com.gblninja.tag.type;

import com.gblninja.tag.GblType;
import com.gblninja.tag.Tag;
import com.gblninja.tag.TagHeader;
import com.gblninja.tag.TagWithHeader;

public class GblSeUpgrade implements Tag, TagWithHeader {
    private final TagHeader tagHeader;
    private final GblType tagType;
    private final long blobSize;
    private final long version;
    private final byte[] data;
    private final byte[] tagData;

    public GblSeUpgrade(TagHeader tagHeader, GblType tagType, long blobSize,
                        long version, byte[] data, byte[] tagData) {
        this.tagHeader = tagHeader;
        this.tagType = tagType;
        this.blobSize = blobSize;
        this.version = version;
        this.data = data;
        this.tagData = tagData;
    }

    @Override
    public GblType getTagType() {
        return tagType;
    }

    @Override
    public Tag copy() {
        return new GblSeUpgrade(tagHeader, tagType, blobSize, version, data, new byte[0]);
    }

    @Override
    public TagHeader getTagHeader() {
        return tagHeader;
    }

    @Override
    public byte[] getTagData() {
        return tagData;
    }

    public long getBlobSize() {
        return blobSize;
    }

    public long getVersion() {
        return version;
    }

    public byte[] getData() {
        return data;
    }
}