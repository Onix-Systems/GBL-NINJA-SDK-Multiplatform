package com.gblninja.tag.type.encryption;


import com.gblninja.tag.GblType;
import com.gblninja.tag.Tag;
import com.gblninja.tag.TagHeader;
import com.gblninja.tag.TagWithHeader;

public class GblEncryptionInitAesCcm implements Tag, TagWithHeader {
    private final TagHeader tagHeader;
    private final GblType tagType;
    private final long msgLen;
    private final short nonce;
    private final byte[] tagData;

    public GblEncryptionInitAesCcm(TagHeader tagHeader, GblType tagType,
                                   long msgLen, short nonce, byte[] tagData) {
        this.tagHeader = tagHeader;
        this.tagType = tagType;
        this.msgLen = msgLen;
        this.nonce = nonce;
        this.tagData = tagData;
    }

    @Override
    public GblType getTagType() {
        return tagType;
    }

    @Override
    public Tag copy() {
        return new GblEncryptionInitAesCcm(tagHeader, tagType, msgLen, nonce, new byte[0]);
    }

    @Override
    public TagHeader getTagHeader() {
        return tagHeader;
    }

    @Override
    public byte[] getTagData() {
        return tagData;
    }

    public long getMsgLen() {
        return msgLen;
    }

    public short getNonce() {
        return nonce;
    }
}