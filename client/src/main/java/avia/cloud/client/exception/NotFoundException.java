package avia.cloud.client.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super();
    }
    public NotFoundException(String resourceName, String resourceField, String resourceValue) {
        super(String.format("%s not found with the given input data %s: %s", resourceName,resourceField,resourceValue));
    }
}
