package me.dimitri.libertyweb.utils.exception;

public class HttpErrorException extends Exception {
    public HttpErrorException(String exceptionMsg, Exception e) {
        super(exceptionMsg, e);
    }
}
