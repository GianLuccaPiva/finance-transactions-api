package com.financetransactions.exception;

public class DuplicateDocumentException extends RuntimeException {
    public DuplicateDocumentException() {
        super("Cannot create account");
    }
}