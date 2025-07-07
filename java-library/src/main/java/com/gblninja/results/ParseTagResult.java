package com.gblninja.results;


import com.gblninja.tag.TagHeader;

public abstract class ParseTagResult {
    public static class Success extends ParseTagResult {
        private final TagHeader tagHeader;
        private final byte[] tagData;

        public Success(TagHeader tagHeader, byte[] tagData) {
            this.tagHeader = tagHeader;
            this.tagData = tagData;
        }

        public TagHeader getTagHeader() {
            return tagHeader;
        }

        public byte[] getTagData() {
            return tagData;
        }
    }

    public static class Fatal extends ParseTagResult {
        private final Object error;

        public Fatal() {
            this.error = null;
        }

        public Fatal(Object error) {
            this.error = error;
        }

        public Object getError() {
            return error;
        }
    }
}