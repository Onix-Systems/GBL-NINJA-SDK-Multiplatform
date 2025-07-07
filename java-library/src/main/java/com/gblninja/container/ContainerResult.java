package com.gblninja.container;

public abstract class ContainerResult<T> {

    public static class Success<T> extends ContainerResult<T> {
        private final T data;

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }
    }

    // Make Error generic but create static factory methods
    public static class Error<T> extends ContainerResult<T> {
        private final String message;
        private final ContainerErrorCode code;

        Error(String message, ContainerErrorCode code) {
            this.message = message;
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public ContainerErrorCode getCode() {
            return code;
        }

        // Static factory method that can return any type
        public static <U> Error<U> create(String message, ContainerErrorCode code) {
            return new Error<>(message, code);
        }
    }
}