package com.gblninja.tag.type.application;

import com.gblninja.tag.GblType;
import com.gblninja.tag.Tag;
import com.gblninja.tag.TagHeader;
import com.gblninja.tag.TagWithHeader;

public class GblApplication implements Tag, TagWithHeader {
    public static final int APP_LENGTH = 13;

    private final TagHeader tagHeader;
    private final GblType tagType;
    private final ApplicationData applicationData;
    private final byte[] tagData;

    public GblApplication(TagHeader tagHeader, GblType tagType,
                          ApplicationData applicationData, byte[] tagData) {
        this.tagHeader = tagHeader;
        this.tagType = tagType;
        this.applicationData = applicationData;
        this.tagData = tagData;
    }

    @Override
    public GblType getTagType() {
        return tagType;
    }

    @Override
    public Tag copy() {
        return new GblApplication(tagHeader, tagType, applicationData, new byte[0]);
    }

    @Override
    public TagHeader getTagHeader() {
        return tagHeader;
    }

    @Override
    public byte[] getTagData() {
        return tagData;
    }

    public ApplicationData getApplicationData() {
        return applicationData;
    }
}