package com.mgaigalas.cowboys.exception;

/**
 * @author Marius Gaigalas
 */
public class NoCowboysFoundException extends RuntimeException {
    public NoCowboysFoundException(String message) {
        super(message);
    }
}
