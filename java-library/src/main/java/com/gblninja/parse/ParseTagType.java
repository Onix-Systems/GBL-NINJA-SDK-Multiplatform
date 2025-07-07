package com.gblninja.parse;

import com.gblninja.tag.DefaultTag;
import com.gblninja.tag.GblType;
import com.gblninja.tag.Tag;
import com.gblninja.tag.TagHeader;
import com.gblninja.tag.type.GblBootloader;
import com.gblninja.tag.type.GblEnd;
import com.gblninja.tag.type.GblEraseProg;
import com.gblninja.tag.type.GblHeader;
import com.gblninja.tag.type.GblMetadata;
import com.gblninja.tag.type.GblProg;
import com.gblninja.tag.type.GblProgLz4;
import com.gblninja.tag.type.GblProgLzma;
import com.gblninja.tag.type.GblSeUpgrade;
import com.gblninja.tag.type.application.ApplicationData;
import com.gblninja.tag.type.application.GblApplication;
import com.gblninja.tag.type.certificate.ApplicationCertificate;
import com.gblninja.tag.type.certificate.GblCertificateEcdsaP256;
import com.gblninja.tag.type.certificate.GblSignatureEcdsaP256;
import com.gblninja.tag.type.encryption.GblEncryptionData;
import com.gblninja.tag.type.encryption.GblEncryptionInitAesCcm;
import com.gblninja.utils.ByteUtils;

public class ParseTagType {
    public static Tag parseTagType(long tagId, long length, byte[] byteArray) {
        GblType tagType = GblType.fromValue(tagId);
        TagHeader tagHeader = new TagHeader(tagId, length);

        if (tagType == null) {
            return new DefaultTag(
                    ByteUtils.copyArray(byteArray),
                    tagHeader,
                    GblType.TAG
            );
        }

        switch (tagType) {
            case HEADER_V3:
                long version = Integer.toUnsignedLong(ByteUtils.getIntFromBytes(byteArray, 0, 4).getInt());
                long gblType = Integer.toUnsignedLong(ByteUtils.getIntFromBytes(byteArray, 4, 4).getInt());
                return new GblHeader(tagHeader, tagType, version, gblType, byteArray);

            case BOOTLOADER:
                return new GblBootloader(
                        tagHeader,
                        tagType,
                        Integer.toUnsignedLong(ByteUtils.getIntFromBytes(byteArray, 0, 4).getInt()),
                        Integer.toUnsignedLong(ByteUtils.getIntFromBytes(byteArray, 4, 4).getInt()),
                        copyRange(byteArray, 8, byteArray.length),
                        byteArray
                );

            case APPLICATION:
                ApplicationData appData = new ApplicationData(
                        Integer.toUnsignedLong(ByteUtils.getIntFromBytes(byteArray, 0, 4).getInt()),
                        Integer.toUnsignedLong(ByteUtils.getIntFromBytes(byteArray, 4, 4).getInt()),
                        Integer.toUnsignedLong(ByteUtils.getIntFromBytes(byteArray, 8, 4).getInt()),
                        (short) (byteArray[12] & 0xFF)
                );
                return new GblApplication(tagHeader, tagType, appData, byteArray);

            case METADATA:
                return new GblMetadata(tagHeader, tagType, byteArray, byteArray);

            case PROG:
                return new GblProg(
                        tagHeader,
                        tagType,
                        Integer.toUnsignedLong(ByteUtils.getIntFromBytes(byteArray, 0, 4).getInt()),
                        copyRange(byteArray, 4, byteArray.length),
                        byteArray
                );

            case PROG_LZ4:
                return new GblProgLz4(tagHeader, tagType, byteArray);

            case PROG_LZMA:
                return new GblProgLzma(tagHeader, tagType, byteArray);

            case ERASEPROG:
                return new GblEraseProg(tagHeader, tagType, byteArray);

            case SE_UPGRADE:
                return new GblSeUpgrade(
                        tagHeader,
                        tagType,
                        Integer.toUnsignedLong(ByteUtils.getIntFromBytes(byteArray, 0, 4).getInt()),
                        Integer.toUnsignedLong(ByteUtils.getIntFromBytes(byteArray, 4, 4).getInt()),
                        copyRange(byteArray, 8, byteArray.length),
                        byteArray
                );

            case END:
                return new GblEnd(
                        tagHeader,
                        tagType,
                        Integer.toUnsignedLong(ByteUtils.getIntFromBytes(byteArray, 0, 4).getInt()),
                        byteArray
                );

            case ENCRYPTION_DATA:
                return new GblEncryptionData(
                        tagHeader,
                        tagType,
                        copyRange(byteArray, 8, byteArray.length),
                        byteArray
                );

            case ENCRYPTION_INIT:
                return new GblEncryptionInitAesCcm(
                        tagHeader,
                        tagType,
                        Integer.toUnsignedLong(ByteUtils.getIntFromBytes(byteArray, 0, 4).getInt()),
                        (short) (byteArray[4] & 0xFF),
                        byteArray
                );

            case SIGNATURE_ECDSA_P256:
                return new GblSignatureEcdsaP256(
                        tagHeader,
                        tagType,
                        byteArray,
                        (short) (byteArray[0] & 0xFF),
                        (short) (byteArray[1] & 0xFF)
                );

            case CERTIFICATE_ECDSA_P256:
                ApplicationCertificate certificate = new ApplicationCertificate(
                        (short) (byteArray[0] & 0xFF),
                        (short) (byteArray[1] & 0xFF),
                        (short) (byteArray[2] & 0xFF),
                        Integer.toUnsignedLong(ByteUtils.getIntFromBytes(byteArray, 3, 4).getInt()),
                        (short) (byteArray[7] & 0xFF)
                );
                return new GblCertificateEcdsaP256(tagHeader, tagType, byteArray, certificate);

            default:
                return new DefaultTag(ByteUtils.copyArray(byteArray), tagHeader, GblType.TAG);
        }
    }

    private static byte[] copyRange(byte[] array, int start, int end) {
        if (start >= end || start >= array.length) return new byte[0];
        int actualEnd = Math.min(end, array.length);
        byte[] result = new byte[actualEnd - start];
        System.arraycopy(array, start, result, 0, actualEnd - start);
        return result;
    }
}