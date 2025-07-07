package com.gblninja.tag;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TagHeader {
    private final long id;
    private final long length;

    public TagHeader(long id, long length) {
        this.id = id;
        this.length = length;
    }

    public long getId() {
        return id;
    }

    public long getLength() {
        return length;
    }

    public byte[] content() {
        ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt((int) (id & 0xFFFFFFFFL));
        buffer.putInt((int) (length & 0xFFFFFFFFL));
        return buffer.array();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagHeader tagHeader = (TagHeader) o;
        return id == tagHeader.id && length == tagHeader.length;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id) * 31 + Long.hashCode(length);
    }
}