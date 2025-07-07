package com.gblninja.encode;


import com.gblninja.tag.GblType;
import com.gblninja.tag.Tag;
import com.gblninja.tag.TagHeader;
import com.gblninja.tag.TagWithHeader;
import com.gblninja.tag.type.GblBootloader;
import com.gblninja.tag.type.GblEnd;
import com.gblninja.tag.type.GblEraseProg;
import com.gblninja.tag.type.GblHeader;
import com.gblninja.tag.type.GblMetadata;
import com.gblninja.tag.type.GblProg;
import com.gblninja.tag.type.GblProgLz4;
import com.gblninja.tag.type.GblProgLzma;
import com.gblninja.tag.type.GblSeUpgrade;
import com.gblninja.tag.type.application.GblApplication;
import com.gblninja.tag.type.certificate.GblCertificateEcdsaP256;
import com.gblninja.tag.type.certificate.GblSignatureEcdsaP256;
import com.gblninja.tag.type.encryption.GblEncryptionData;
import com.gblninja.tag.type.encryption.GblEncryptionInitAesCcm;
import com.gblninja.tag.type.version.GblVersionDependency;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.zip.CRC32;

public class EncodeTags {
    private static final int TAG_ID_SIZE = 4;
    private static final int TAG_LENGTH_SIZE = 4;

    public static byte[] encodeTags(List<Tag> tags) {
        int totalSize = calculateTotalSize(tags);
        ByteBuffer buffer = ByteBuffer.allocate(totalSize);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        for (Tag tag : tags) {
            if (!(tag instanceof TagWithHeader)) continue;

            TagWithHeader tagWithHeader = (TagWithHeader) tag;
            buffer.putInt((int) (tagWithHeader.getTagHeader().getId() & 0xFFFFFFFFL));
            buffer.putInt((int) (tagWithHeader.getTagHeader().getLength() & 0xFFFFFFFFL));

            byte[] tagData = generateTagData(tag);
            buffer.put(tagData);
        }

        return buffer.array();
    }

    public static byte[] generateTagData(Tag tag) {
        if (tag instanceof GblHeader) {
            GblHeader header = (GblHeader) tag;
            ByteBuffer buffer = ByteBuffer.allocate(8)
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .putInt((int) (header.getVersion() & 0xFFFFFFFFL))
                    .putInt((int) (header.getGblType() & 0xFFFFFFFFL));
            return buffer.array();
        }

        if (tag instanceof GblBootloader) {
            GblBootloader bootloader = (GblBootloader) tag;
            ByteBuffer buffer = ByteBuffer.allocate((int) bootloader.getTagHeader().getLength())
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .putInt((int) (bootloader.getBootloaderVersion() & 0xFFFFFFFFL))
                    .putInt((int) (bootloader.getAddress() & 0xFFFFFFFFL));
            buffer.put(bootloader.getData());
            return buffer.array();
        }

        if (tag instanceof GblApplication) {
            GblApplication app = (GblApplication) tag;
            ByteBuffer buffer = ByteBuffer.allocate((int) app.getTagHeader().getLength())
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .putInt((int) (app.getApplicationData().getType() & 0xFFFFFFFFL))
                    .putInt((int) (app.getApplicationData().getVersion() & 0xFFFFFFFFL))
                    .putInt((int) (app.getApplicationData().getCapabilities() & 0xFFFFFFFFL))
                    .put((byte) app.getApplicationData().getProductId());

            if (app.getTagHeader().getLength() > 13L) {
                byte[] remainingData = new byte[(int) app.getTagHeader().getLength() - 13];
                System.arraycopy(app.getTagData(), 13, remainingData, 0, remainingData.length);
                buffer.put(remainingData);
            }
            return buffer.array();
        }

        if (tag instanceof GblProg) {
            GblProg prog = (GblProg) tag;
            ByteBuffer buffer = ByteBuffer.allocate((int) prog.getTagHeader().getLength())
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .putInt((int) (prog.getFlashStartAddress() & 0xFFFFFFFFL));
            buffer.put(prog.getData());
            return buffer.array();
        }

        if (tag instanceof GblSeUpgrade) {
            GblSeUpgrade seUpgrade = (GblSeUpgrade) tag;
            ByteBuffer buffer = ByteBuffer.allocate((int) seUpgrade.getTagHeader().getLength())
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .putInt((int) (seUpgrade.getBlobSize() & 0xFFFFFFFFL))
                    .putInt((int) (seUpgrade.getVersion() & 0xFFFFFFFFL));
            buffer.put(seUpgrade.getData());
            return buffer.array();
        }

        if (tag instanceof GblEnd) {
            GblEnd end = (GblEnd) tag;
            System.out.println("Found end tag " + end.getTagHeader().getLength());
            ByteBuffer buffer = ByteBuffer.allocate((int) end.getTagHeader().getLength())
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .putInt((int) (end.getGblCrc() & 0xFFFFFFFFL));
            return buffer.array();
        }

        if (tag instanceof GblMetadata) {
            GblMetadata metadata = (GblMetadata) tag;
            ByteBuffer buffer = ByteBuffer.allocate((int) metadata.getTagHeader().getLength())
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .put(metadata.getMetaData());
            return buffer.array();
        }

        if (tag instanceof GblProgLz4) {
            GblProgLz4 progLz4 = (GblProgLz4) tag;
            ByteBuffer buffer = ByteBuffer.allocate((int) progLz4.getTagHeader().getLength())
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .put(progLz4.getTagData());
            return buffer.array();
        }

        if (tag instanceof GblProgLzma) {
            GblProgLzma progLzma = (GblProgLzma) tag;
            ByteBuffer buffer = ByteBuffer.allocate((int) progLzma.getTagHeader().getLength())
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .put(progLzma.getTagData());
            return buffer.array();
        }

        if (tag instanceof GblEraseProg) {
            GblEraseProg eraseProg = (GblEraseProg) tag;
            ByteBuffer buffer = ByteBuffer.allocate((int) eraseProg.getTagHeader().getLength())
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .put(eraseProg.getTagData());
            return buffer.array();
        }

        if (tag instanceof GblCertificateEcdsaP256) {
            GblCertificateEcdsaP256 cert = (GblCertificateEcdsaP256) tag;
            ByteBuffer buffer = ByteBuffer.allocate((int) cert.getTagHeader().getLength())
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .put((byte) cert.getCertificate().getStructVersion())
                    .put((byte) cert.getCertificate().getFlags())
                    .put((byte) cert.getCertificate().getKey())
                    .putInt((int) (cert.getCertificate().getVersion() & 0xFFFFFFFFL))
                    .put((byte) cert.getCertificate().getSignature());
            return buffer.array();
        }

        if (tag instanceof GblSignatureEcdsaP256) {
            GblSignatureEcdsaP256 sig = (GblSignatureEcdsaP256) tag;
            ByteBuffer buffer = ByteBuffer.allocate((int) sig.getTagHeader().getLength())
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .put((byte) sig.getR())
                    .put((byte) sig.getS());
            return buffer.array();
        }

        if (tag instanceof GblEncryptionData) {
            GblEncryptionData encData = (GblEncryptionData) tag;
            ByteBuffer buffer = ByteBuffer.allocate((int) encData.getTagHeader().getLength())
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .put(encData.getEncryptedGblData());
            return buffer.array();
        }

        if (tag instanceof GblEncryptionInitAesCcm) {
            GblEncryptionInitAesCcm encInit = (GblEncryptionInitAesCcm) tag;
            ByteBuffer buffer = ByteBuffer.allocate((int) encInit.getTagHeader().getLength())
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .putInt((int) (encInit.getMsgLen() & 0xFFFFFFFFL))
                    .put((byte) encInit.getNonce());
            return buffer.array();
        }

        if (tag instanceof GblVersionDependency) {
            GblVersionDependency versionDep = (GblVersionDependency) tag;
            ByteBuffer buffer = ByteBuffer.allocate(8)
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .putInt((int) (versionDep.getImageType().getValue() & 0xFFFFFFFFL))
                    .put((byte) versionDep.getStatement())
                    .putShort((short) versionDep.getReversed())
                    .putInt((int) (versionDep.getVersion() & 0xFFFFFFFFL));
            return buffer.array();
        }

        return new byte[0];
    }

    private static int calculateTotalSize(List<Tag> tags) {
        int totalSize = 0;
        for (Tag tag : tags) {
            if (tag instanceof TagWithHeader) {
                TagWithHeader tagWithHeader = (TagWithHeader) tag;
                totalSize += TAG_ID_SIZE + TAG_LENGTH_SIZE + tagWithHeader.getTagHeader().getLength();
            }
        }
        return totalSize;
    }

    public static GblEnd createEndTagWithCrc(List<Tag> tags) {
        CRC32 crc = new CRC32();

        for (Tag tag : tags) {
            if (!(tag instanceof TagWithHeader)) continue;

            TagWithHeader tagWithHeader = (TagWithHeader) tag;
            
            byte[] tagIdBytes = ByteBuffer.allocate(4)
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .putInt((int) (tagWithHeader.getTagHeader().getId() & 0xFFFFFFFFL))
                    .array();

            byte[] tagLengthBytes = ByteBuffer.allocate(4)
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .putInt((int) (tagWithHeader.getTagHeader().getLength() & 0xFFFFFFFFL))
                    .array();

            byte[] tagData = generateTagData(tag);

            crc.update(tagIdBytes);
            crc.update(tagLengthBytes);
            crc.update(tagData);
        }

        long endTagId = GblType.END.getValue();
        int endTagLength = TAG_LENGTH_SIZE;

        byte[] endTagIdBytes = ByteBuffer.allocate(TAG_LENGTH_SIZE)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt((int) (endTagId & 0xFFFFFFFFL))
                .array();

        byte[] endTagLengthBytes = ByteBuffer.allocate(TAG_LENGTH_SIZE)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(endTagLength)
                .array();

        crc.update(endTagIdBytes);
        crc.update(endTagLengthBytes);

        long crcValue = Integer.toUnsignedLong((int) crc.getValue());
        byte[] crcBytes = ByteBuffer.allocate(TAG_LENGTH_SIZE)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt((int) (crcValue & 0xFFFFFFFFL))
                .array();

        return new GblEnd(
                new TagHeader(GblType.END.getValue(), TAG_LENGTH_SIZE),
                GblType.END,
                crcValue,
                crcBytes
        );
    }

    public static byte[] encodeTagsWithEndTag(List<Tag> tags) {
        List<Tag> tagsWithoutEnd = tags.stream()
                .filter(tag -> !(tag instanceof GblEnd))
                .collect(java.util.stream.Collectors.toList());

        GblEnd endTag = createEndTagWithCrc(tagsWithoutEnd);
        List<Tag> finalTags = new java.util.ArrayList<>(tagsWithoutEnd);
        finalTags.add(endTag);

        return encodeTags(finalTags);
    }
}