package com.gblninja.results;


import com.gblninja.tag.Tag;

import java.util.List;

public abstract class ParseResult {
    public static class Success extends ParseResult {
        private final List<Tag> resultList;

        public Success(List<Tag> resultList) {
            this.resultList = resultList;
        }

        public List<Tag> getResultList() {
            return resultList;
        }
    }

    public static class Fatal extends ParseResult {
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
