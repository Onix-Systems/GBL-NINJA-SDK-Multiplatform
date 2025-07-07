package com.gblninja.tag;

public interface TagWithHeader {
    TagHeader getTagHeader();
    byte[] getTagData();
}