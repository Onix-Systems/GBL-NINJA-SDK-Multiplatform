package com.gblninja.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteUtils {
    public static byte[] append(byte[] first, byte[] second) {
        byte[] result = new byte[first.length + second.length];
        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static void putUIntToByteArray(byte[] array, int offset, long value) {
        ByteBuffer.wrap(array, offset, 4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt((int) (value & 0xFFFFFFFFL));
    }

    public static byte[] copyArray(byte[] original) {
        if (original == null) return null;
        byte[] copy = new byte[original.length];
        System.arraycopy(original, 0, copy, 0, original.length);
        return copy;
    }

    public static ByteBuffer getIntFromBytes(byte[] byteArray, int offset, int length) {
        return ByteBuffer.wrap(byteArray, offset, length)
                .order(ByteOrder.LITTLE_ENDIAN);
    }

    public static byte[] longToByteArray(long value) {
        return longToByteArray(value, ByteOrder.LITTLE_ENDIAN);
    }

    public static byte[] longToByteArray(long value, ByteOrder order) {
        return ByteBuffer.allocate(4)
                .order(order)
                .putInt((int) (value & 0xFFFFFFFFL))
                .array();
    }
}