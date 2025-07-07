package com.gblninja.tag.type.certificate;

import com.gblninja.tag.GblType;
import com.gblninja.tag.Tag;
import com.gblninja.tag.TagHeader;
import com.gblninja.tag.TagWithHeader;

public class GblSignatureEcdsaP256 implements Tag, TagWithHeader {
    private final TagHeader tagHeader;
    private final GblType tagType;
    private final byte[] tagData;
    private final short r;
    private final short s;

    public GblSignatureEcdsaP256(TagHeader tagHeader, GblType tagType,
                                 byte[] tagData, short r, short s) {
        this.tagHeader = tagHeader;
        this.tagType = tagType;
        this.tagData = tagData;
        this.r = r;
        this.s = s;
    }

    @Override
    public GblType getTagType() {
        return tagType;
    }

    @Override
    public Tag copy() {
        return new GblSignatureEcdsaP256(tagHeader, tagType, tagData, r, s);
    }

    @Override
    public TagHeader getTagHeader() {
        return tagHeader;
    }

    @Override
    public byte[] getTagData() {
        return tagData;
    }

    public short getR() {
        return r;
    }

    public short getS() {
        return s;
    }
}