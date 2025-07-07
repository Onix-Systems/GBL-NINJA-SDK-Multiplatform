package com.gblninja.tag.type.encryption;


import com.gblninja.tag.GblType;
import com.gblninja.tag.Tag;
import com.gblninja.tag.TagHeader;
import com.gblninja.tag.TagWithHeader;

public class GblEncryptionData implements Tag, TagWithHeader {
    private final TagHeader tagHeader;
    private final GblType tagType;
    private final byte[] tagData;
    private final byte[] encryptedGblData;

    public GblEncryptionData(TagHeader tagHeader, GblType tagType,
                             byte[] encryptedGblData, byte[] tagData) {
        this.tagHeader = tagHeader;
        this.tagType = tagType;
        this.tagData = tagData;
        this.encryptedGblData = encryptedGblData;
    }

    @Override
    public GblType getTagType() {
        return tagType;
    }

    @Override
    public Tag copy() {
        return new GblEncryptionData(tagHeader, tagType, encryptedGblData, tagData);
    }

    @Override
    public TagHeader getTagHeader() {
        return tagHeader;
    }

    @Override
    public byte[] getTagData() {
        return tagData;
    }

    public byte[] getEncryptedGblData() {
        return encryptedGblData;
    }
}