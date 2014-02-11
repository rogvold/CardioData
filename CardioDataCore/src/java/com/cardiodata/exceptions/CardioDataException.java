package com.cardiodata.exceptions;


/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
public class CardioDataException extends Exception {

    private Integer errorCode;

    /**
     * Creates a new instance of
     * <code>CardioDataException</code> without detail message.
     */
    public CardioDataException() {
    }

    public CardioDataException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CardioDataException(String msg) {
        super(msg);
        this.errorCode = ResponseConstants.NORMAL_ERROR_CODE;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
