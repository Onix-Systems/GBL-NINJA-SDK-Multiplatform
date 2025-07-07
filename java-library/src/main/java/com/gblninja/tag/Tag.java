package com.gblninja.tag;

import com.gblninja.encode.EncodeTags;

public interface Tag {
    GblType getTagType();
    Tag copy();

    default byte[] content() {
        return EncodeTags.generateTagData(this);
    }
}