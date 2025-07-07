package com.gblninja.tag;

public enum GblType {
    HEADER_V3(0x03A617EBL),
    BOOTLOADER(0xF50909F5L),
    APPLICATION(0xF40A0AF4L),
    METADATA(0xF60808F6L),
    PROG(0xFE0101FEL),
    PROG_LZ4(0xFD0505FDL),
    PROG_LZMA(0xFD0707FDL),
    ERASEPROG(0xFD0303FDL),
    SE_UPGRADE(0x5EA617EBL),
    END(0xFC0404FCL),
    TAG(0L),
    ENCRYPTION_DATA(0xF90707F9L),
    ENCRYPTION_INIT(0xFA0606FAL),
    SIGNATURE_ECDSA_P256(0xF70A0AF7L),
    CERTIFICATE_ECDSA_P256(0xF30B0BF3L),
    VERSION_DEPENDENCY(0x76A617EBL);

    private final long value;

    GblType(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public static GblType fromValue(long value) {
        for (GblType type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }
}