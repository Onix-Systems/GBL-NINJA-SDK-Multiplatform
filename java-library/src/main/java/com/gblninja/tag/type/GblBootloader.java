package com.gblninja.tag.type;


import com.gblninja.tag.GblType;
import com.gblninja.tag.Tag;
import com.gblninja.tag.TagHeader;
import com.gblninja.tag.TagWithHeader;

public class GblBootloader implements Tag, TagWithHeader {
    private final TagHeader tagHeader;
    private final GblType tagType;
    private final long bootloaderVersion;
    private final long address;
    private final byte[] data;
    private final byte[] tagData;

    public GblBootloader(TagHeader tagHeader, GblType tagType, long bootloaderVersion,
                         long address, byte[] data, byte[] tagData) {
        this.tagHeader = tagHeader;
        this.tagType = tagType;
        this.bootloaderVersion = bootloaderVersion;
        this.address = address;
        this.data = data;
        this.tagData = tagData;
    }

    @Override
    public GblType getTagType() {
        return tagType;
    }

    @Override
    public Tag copy() {
        return new GblBootloader(tagHeader, tagType, bootloaderVersion, address, data, new byte[0]);
    }

    @Override
    public TagHeader getTagHeader() {
        return tagHeader;
    }

    @Override
    public byte[] getTagData() {
        return tagData;
    }

    public long getBootloaderVersion() {
        return bootloaderVersion;
    }

    public long getAddress() {
        return address;
    }

    public byte[] getData() {
        return data;
    }
}