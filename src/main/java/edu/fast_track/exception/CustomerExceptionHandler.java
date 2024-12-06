package edu.fast_track.exception;

public class CustomerExceptionHandler extends RuntimeException{
    public CustomerExceptionHandler(String message) {
        super(message);
    }
}
