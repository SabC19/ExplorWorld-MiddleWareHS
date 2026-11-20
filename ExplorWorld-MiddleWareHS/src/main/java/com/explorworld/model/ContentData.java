package com.explorworld.model;

public class ContentData {

    private ContentMetadata metadata;
    private byte[] raw;

    public ContentData() {
    }

    public ContentData(ContentMetadata metadata, byte[] raw) {
        this.metadata = metadata;
        this.raw = raw;
    }

    public ContentMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ContentMetadata metadata) {
        this.metadata = metadata;
    }

    public byte[] getRaw() {
        return raw;
    }

    public void setRaw(byte[] raw) {
        this.raw = raw;
    }
}