package me.dimitri.libertyweb.utils.exception;

public class HttpErrorException extends Exception {
    public HttpErrorException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
