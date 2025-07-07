package com.gblninja.parse;

import com.gblninja.results.ParseTagResult;
import com.gblninja.tag.TagHeader;
import com.gblninja.utils.ByteUtils;

public class ParseTag {
    private static final int TAG_ID_SIZE = 4;
    private static final int TAG_LENGTH_SIZE = 4;

    public static ParseTagResult parseTag(byte[] byteArray, int offset) {
        if (offset < 0 || offset + TAG_ID_SIZE + TAG_LENGTH_SIZE > byteArray.length) {
            return new ParseTagResult.Fatal("Invalid offset: " + offset);
        }

        long tagId = Integer.toUnsignedLong(ByteUtils.getIntFromBytes(byteArray, offset, TAG_ID_SIZE).getInt());
        int tagLength = ByteUtils.getIntFromBytes(byteArray, offset + TAG_ID_SIZE, TAG_LENGTH_SIZE).getInt();

        if (offset + TAG_ID_SIZE + TAG_LENGTH_SIZE + tagLength > byteArray.length) {
            return new ParseTagResult.Fatal("Invalid tag length: " + tagLength);
        }

        byte[] tagData = new byte[tagLength];
        System.arraycopy(byteArray, offset + TAG_ID_SIZE + TAG_LENGTH_SIZE, tagData, 0, tagLength);

        TagHeader tagHeader = new TagHeader(tagId, Integer.toUnsignedLong(tagLength));

        return new ParseTagResult.Success(tagHeader, tagData);
    }
}