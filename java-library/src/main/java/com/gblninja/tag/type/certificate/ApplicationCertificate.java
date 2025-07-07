package com.gblninja.tag.type.certificate;

public class ApplicationCertificate {
    private final short structVersion;
    private final short flags;
    private final short key;
    private final long version;
    private final short signature;

    public ApplicationCertificate(short structVersion, short flags, short key,
                                  long version, short signature) {
        this.structVersion = structVersion;
        this.flags = flags;
        this.key = key;
        this.version = version;
        this.signature = signature;
    }

    public short getStructVersion() {
        return structVersion;
    }

    public short getFlags() {
        return flags;
    }

    public short getKey() {
        return key;
    }

    public long getVersion() {
        return version;
    }

    public short getSignature() {
        return signature;
    }
}