package com.gblninja;

import com.gblninja.container.ContainerResult;
import com.gblninja.container.TagContainer;
import com.gblninja.encode.EncodeTags;
import com.gblninja.parse.ParseTag;
import com.gblninja.parse.ParseTagType;
import com.gblninja.results.ParseResult;
import com.gblninja.results.ParseTagResult;
import com.gblninja.tag.DefaultTag;
import com.gblninja.tag.GblType;
import com.gblninja.tag.Tag;
import com.gblninja.tag.TagHeader;
import com.gblninja.tag.type.GblBootloader;
import com.gblninja.tag.type.GblEnd;
import com.gblninja.tag.type.GblEraseProg;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Gbl {
    public static final int HEADER_SIZE = 8;
    public static final int TAG_ID_SIZE = 4;
    public static final int TAG_LENGTH_SIZE = 4;

    public ParseResult parseByteArray(byte[] byteArray) {
        int offset = 0;
        int size = byteArray.length;
        List<Tag> rawTags = new ArrayList<>();

        if (byteArray.length < HEADER_SIZE) {
            return new ParseResult.Fatal("File is too small to be a valid gbl file. Expected at least " + HEADER_SIZE + " bytes, got " + byteArray.length + " bytes.");
        }

        while (offset < size) {
            ParseTagResult result = ParseTag.parseTag(byteArray, offset);

            if (result instanceof ParseTagResult.Fatal) {
                break;
            }

            if (result instanceof ParseTagResult.Success) {
                ParseTagResult.Success successResult = (ParseTagResult.Success) result;
                TagHeader header = successResult.getTagHeader();
                byte[] data = successResult.getTagData();

                try {
                    Tag parsedTag = ParseTagType.parseTagType(
                            header.getId(),
                            header.getLength(),
                            data
                    );

                    rawTags.add(parsedTag);
                    offset += TAG_ID_SIZE + TAG_LENGTH_SIZE + (int) (header.getLength() & 0xFFFFFFFFL);
                } catch (Exception e) {
                    break;
                }
            }
        }

        return new ParseResult.Success(rawTags);
    }

    public byte[] encode(List<Tag> tags) {
        List<Tag> tagsWithoutEnd = new ArrayList<>();
        for (Tag tag : tags) {
            if (!(tag instanceof GblEnd)) {
                tagsWithoutEnd.add(tag);
            }
        }

        GblEnd endTag = EncodeTags.createEndTagWithCrc(tagsWithoutEnd);
        List<Tag> finalTags = new ArrayList<>(tagsWithoutEnd);
        finalTags.add(endTag);

        return EncodeTags.encodeTags(finalTags);
    }

    public static class GblBuilder {
        private final TagContainer container = new TagContainer();

        public GblBuilder() {}

        public static GblBuilder create() {
            GblBuilder builder = new GblBuilder();
            builder.container.create();
            return builder;
        }

        public static GblBuilder empty() {
            return new GblBuilder();
        }

        public GblBuilder encryptionData(byte[] encryptedGblData) {
            GblEncryptionData tag = new GblEncryptionData(
                    new TagHeader(
                            GblType.ENCRYPTION_DATA.getValue(),
                            encryptedGblData.length & 0xFFFFFFFFL
                    ),
                    GblType.ENCRYPTION_DATA,
                    ByteUtils.copyArray(encryptedGblData),
                    ByteUtils.copyArray(encryptedGblData)
            );

            container.add(tag);
            return this;
        }

        public GblBuilder encryptionInit(long msgLen, short nonce) {
            GblEncryptionInitAesCcm tag = new GblEncryptionInitAesCcm(
                    new TagHeader(
                            GblType.ENCRYPTION_INIT.getValue(),
                            5L
                    ),
                    GblType.ENCRYPTION_INIT,
                    msgLen,
                    nonce,
                    generateEncryptionInitTagData(msgLen, nonce)
            );

            container.add(tag);
            return this;
        }

        public GblBuilder signatureEcdsaP256(short r, short s) {
            GblSignatureEcdsaP256 tag = new GblSignatureEcdsaP256(
                    new TagHeader(
                            GblType.SIGNATURE_ECDSA_P256.getValue(),
                            2L
                    ),
                    GblType.SIGNATURE_ECDSA_P256,
                    generateSignatureEcdsaP256TagData(r, s),
                    r,
                    s
            );

            container.add(tag);
            return this;
        }

        public GblBuilder certificateEcdsaP256(ApplicationCertificate certificate) {
            GblCertificateEcdsaP256 tag = new GblCertificateEcdsaP256(
                    new TagHeader(
                            GblType.CERTIFICATE_ECDSA_P256.getValue(),
                            8L
                    ),
                    GblType.CERTIFICATE_ECDSA_P256,
                    generateCertificateEcdsaP256TagData(certificate),
                    certificate
            );

            container.add(tag);
            return this;
        }

        public GblBuilder versionDependency(byte[] dependencyData) {
            DefaultTag tag = new DefaultTag(
                    ByteUtils.copyArray(dependencyData),
                    new TagHeader(
                            GblType.VERSION_DEPENDENCY.getValue(),
                            dependencyData.length & 0xFFFFFFFFL
                    ),
                    GblType.VERSION_DEPENDENCY
            );

            container.add(tag);
            return this;
        }

        public GblBuilder bootloader(long bootloaderVersion, long address, byte[] data) {
            GblBootloader tag = new GblBootloader(
                    new TagHeader(
                            GblType.BOOTLOADER.getValue(),
                            (8L + data.length) & 0xFFFFFFFFL
                    ),
                    GblType.BOOTLOADER,
                    bootloaderVersion,
                    address,
                    data,
                    generateBootloaderTagData(bootloaderVersion, address, data)
            );

            container.add(tag);
            return this;
        }

        public GblBuilder metadata(byte[] metaData) {
            GblMetadata tag = new GblMetadata(
                    new TagHeader(
                            GblType.METADATA.getValue(),
                            metaData.length & 0xFFFFFFFFL
                    ),
                    GblType.METADATA,
                    metaData,
                    ByteUtils.copyArray(metaData)
            );

            container.add(tag);
            return this;
        }

        public GblBuilder prog(long flashStartAddress, byte[] data) {
            GblProg tag = new GblProg(
                    new TagHeader(
                            GblType.PROG.getValue(),
                            (4L + data.length) & 0xFFFFFFFFL
                    ),
                    GblType.PROG,
                    flashStartAddress,
                    data,
                    generateProgTagData(flashStartAddress, data)
            );

            container.add(tag);
            return this;
        }

        public GblBuilder progLz4(long flashStartAddress, byte[] compressedData, long decompressedSize) {
            GblProgLz4 tag = new GblProgLz4(
                    new TagHeader(
                            GblType.PROG_LZ4.getValue(),
                            (8L + compressedData.length) & 0xFFFFFFFFL
                    ),
                    GblType.PROG_LZ4,
                    generateProgLz4TagData(flashStartAddress, compressedData, decompressedSize)
            );

            container.add(tag);
            return this;
        }

        public GblBuilder progLzma(long flashStartAddress, byte[] compressedData, long decompressedSize) {
            GblProgLzma tag = new GblProgLzma(
                    new TagHeader(
                            GblType.PROG_LZMA.getValue(),
                            (8L + compressedData.length) & 0xFFFFFFFFL
                    ),
                    GblType.PROG_LZMA,
                    generateProgLzmaTagData(flashStartAddress, compressedData, decompressedSize)
            );

            container.add(tag);
            return this;
        }

        public GblBuilder seUpgrade(long version, byte[] data) {
            long blobSize = data.length & 0xFFFFFFFFL;
            GblSeUpgrade tag = new GblSeUpgrade(
                    new TagHeader(
                            GblType.SE_UPGRADE.getValue(),
                            (8L + blobSize) & 0xFFFFFFFFL
                    ),
                    GblType.SE_UPGRADE,
                    blobSize,
                    version,
                    data,
                    generateSeUpgradeTagData(blobSize, version, data)
            );

            container.add(tag);
            return this;
        }

        public GblBuilder application() {
            return application(ApplicationData.APP_TYPE, ApplicationData.APP_VERSION,
                    ApplicationData.APP_CAPABILITIES, ApplicationData.APP_PRODUCT_ID, new byte[0]);
        }

        public GblBuilder application(long type, long version, long capabilities, short productId, byte[] additionalData) {
            ApplicationData applicationData = new ApplicationData(type, version, capabilities, productId);
            byte[] tagData = ByteUtils.append(applicationData.content(), additionalData);

            GblApplication tag = new GblApplication(
                    new TagHeader(
                            GblType.APPLICATION.getValue(),
                            tagData.length & 0xFFFFFFFFL
                    ),
                    GblType.APPLICATION,
                    applicationData,
                    tagData
            );

            container.add(tag);
            return this;
        }

        public GblBuilder eraseProg() {
            GblEraseProg tag = new GblEraseProg(
                    new TagHeader(
                            GblType.ERASEPROG.getValue(),
                            8L
                    ),
                    GblType.ERASEPROG,
                    new byte[8]
            );

            container.add(tag);
            return this;
        }

        public List<Tag> get() {
            ContainerResult<List<Tag>> list = container.build();
            if (list instanceof ContainerResult.Success) {
                return ((ContainerResult.Success<List<Tag>>) list).getData();
            }
            return new ArrayList<>();
        }

        public List<Tag> buildToList() {
            List<Tag> tags = getOrDefault(container.build(), new ArrayList<>());
            List<Tag> tagsWithoutEnd = new ArrayList<>();
            for (Tag tag : tags) {
                if (!(tag instanceof GblEnd)) {
                    tagsWithoutEnd.add(tag);
                }
            }
            GblEnd endTag = EncodeTags.createEndTagWithCrc(tagsWithoutEnd);
            List<Tag> result = new ArrayList<>(tagsWithoutEnd);
            result.add(endTag);
            return result;
        }

        public byte[] buildToByteArray() {
            List<Tag> tags = buildToList();
            return EncodeTags.encodeTags(tags);
        }

        public boolean hasTag(GblType tagType) {
            return container.hasTag(tagType);
        }

        public Tag getTag(GblType tagType) {
            return container.getTag(tagType);
        }

        public ContainerResult<Void> removeTag(Tag tag) {
            return container.remove(tag);
        }

        public ContainerResult<Void> clear() {
            return container.clear();
        }

        public int size() {
            return container.size();
        }

        public boolean isEmpty() {
            return container.isEmpty();
        }

        public Set<GblType> getTagTypes() {
            return container.getTagTypes();
        }

        private byte[] generateEncryptionInitTagData(long msgLen, short nonce) {
            byte[] result = new byte[5];
            ByteUtils.putUIntToByteArray(result, 0, msgLen);
            result[4] = (byte) nonce;
            return result;
        }

        private byte[] generateSignatureEcdsaP256TagData(short r, short s) {
            byte[] result = new byte[2];
            result[0] = (byte) r;
            result[1] = (byte) s;
            return result;
        }

        private byte[] generateCertificateEcdsaP256TagData(ApplicationCertificate certificate) {
            byte[] result = new byte[8];
            result[0] = (byte) certificate.getStructVersion();
            result[1] = (byte) certificate.getFlags();
            result[2] = (byte) certificate.getKey();
            ByteUtils.putUIntToByteArray(result, 3, certificate.getVersion());
            result[7] = (byte) certificate.getSignature();
            return result;
        }

        private byte[] generateBootloaderTagData(long bootloaderVersion, long address, byte[] data) {
            byte[] result = new byte[8 + data.length];
            ByteUtils.putUIntToByteArray(result, 0, bootloaderVersion);
            ByteUtils.putUIntToByteArray(result, 4, address);
            System.arraycopy(data, 0, result, 8, data.length);
            return result;
        }

        private byte[] generateProgTagData(long flashStartAddress, byte[] data) {
            byte[] result = new byte[4 + data.length];
            ByteUtils.putUIntToByteArray(result, 0, flashStartAddress);
            System.arraycopy(data, 0, result, 4, data.length);
            return result;
        }

        private byte[] generateProgLz4TagData(long flashStartAddress, byte[] compressedData, long decompressedSize) {
            byte[] result = new byte[8 + compressedData.length];
            ByteUtils.putUIntToByteArray(result, 0, flashStartAddress);
            ByteUtils.putUIntToByteArray(result, 4, decompressedSize);
            System.arraycopy(compressedData, 0, result, 8, compressedData.length);
            return result;
        }

        private byte[] generateProgLzmaTagData(long flashStartAddress, byte[] compressedData, long decompressedSize) {
            byte[] result = new byte[8 + compressedData.length];
            ByteUtils.putUIntToByteArray(result, 0, flashStartAddress);
            ByteUtils.putUIntToByteArray(result, 4, decompressedSize);
            System.arraycopy(compressedData, 0, result, 8, compressedData.length);
            return result;
        }

        private byte[] generateSeUpgradeTagData(long blobSize, long version, byte[] data) {
            byte[] result = new byte[8 + data.length];
            ByteUtils.putUIntToByteArray(result, 0, blobSize);
            ByteUtils.putUIntToByteArray(result, 4, version);
            System.arraycopy(data, 0, result, 8, data.length);
            return result;
        }

        private <T> T getOrDefault(ContainerResult<T> result, T defaultValue) {
            if (result instanceof ContainerResult.Success) {
                return ((ContainerResult.Success<T>) result).getData();
            }
            return defaultValue;
        }
    }
}