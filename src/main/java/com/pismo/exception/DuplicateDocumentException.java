package com.pismo.exception;

public class DuplicateDocumentException extends RuntimeException {
    public DuplicateDocumentException() {
        super("Cannot create account");
    }
}