package xyz.rajik.exception;

public class IncorrectPointException extends RuntimeException{
    @Override
    public String getMessage() {
        return "Incorrect point";
    }
}
