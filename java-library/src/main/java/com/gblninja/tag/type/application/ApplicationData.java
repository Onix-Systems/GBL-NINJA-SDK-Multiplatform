package com.gblninja.tag.type.application;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ApplicationData {
    public static final long APP_TYPE = 32L;
    public static final long APP_VERSION = 5L;
    public static final long APP_CAPABILITIES = 0L;
    public static final short APP_PRODUCT_ID = 54;

    private final long type;
    private final long version;
    private final long capabilities;
    private final short productId;

    public ApplicationData(long type, long version, long capabilities, short productId) {
        this.type = type;
        this.version = version;
        this.capabilities = capabilities;
        this.productId = productId;
    }

    public long getType() {
        return type;
    }

    public long getVersion() {
        return version;
    }

    public long getCapabilities() {
        return capabilities;
    }

    public short getProductId() {
        return productId;
    }

    public byte[] content() {
        ByteBuffer buffer = ByteBuffer.allocate(13).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt((int) (type & 0xFFFFFFFFL));
        buffer.putInt((int) (version & 0xFFFFFFFFL));
        buffer.putInt((int) (capabilities & 0xFFFFFFFFL));
        buffer.put((byte) productId);
        return buffer.array();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationData that = (ApplicationData) o;
        return type == that.type && version == that.version &&
                capabilities == that.capabilities && productId == that.productId;
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(type);
        result = 31 * result + Long.hashCode(version);
        result = 31 * result + Long.hashCode(capabilities);
        result = 31 * result + Short.hashCode(productId);
        return result;
    }
}