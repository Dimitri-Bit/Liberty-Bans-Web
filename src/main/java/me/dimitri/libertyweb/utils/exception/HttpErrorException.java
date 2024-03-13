package me.dimitri.libertyweb.utils.exception;

public class HttpErrorException extends RuntimeException {
    public HttpErrorException(String exceptionMsg, Throwable throwable) {
        super(exceptionMsg, throwable);
    }
}
