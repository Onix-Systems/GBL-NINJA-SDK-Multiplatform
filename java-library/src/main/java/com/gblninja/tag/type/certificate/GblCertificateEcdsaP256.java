package com.gblninja.tag.type.certificate;

import com.gblninja.tag.GblType;
import com.gblninja.tag.Tag;
import com.gblninja.tag.TagHeader;
import com.gblninja.tag.TagWithHeader;

public class GblCertificateEcdsaP256 implements Tag, TagWithHeader {
    private final TagHeader tagHeader;
    private final GblType tagType;
    private final byte[] tagData;
    private final ApplicationCertificate certificate;

    public GblCertificateEcdsaP256(TagHeader tagHeader, GblType tagType,
                                   byte[] tagData, ApplicationCertificate certificate) {
        this.tagHeader = tagHeader;
        this.tagType = tagType;
        this.tagData = tagData;
        this.certificate = certificate;
    }

    @Override
    public GblType getTagType() {
        return tagType;
    }

    @Override
    public Tag copy() {
        return new GblCertificateEcdsaP256(tagHeader, tagType, tagData, certificate);
    }

    @Override
    public TagHeader getTagHeader() {
        return tagHeader;
    }

    @Override
    public byte[] getTagData() {
        return tagData;
    }

    public ApplicationCertificate getCertificate() {
        return certificate;
    }
}