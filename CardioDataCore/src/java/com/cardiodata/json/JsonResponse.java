package com.cardiodata.json;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
public class JsonResponse<T> {

    private Integer responseCode;
    private JsonError error;
    private T data;
    private Long serverTime;

    public JsonResponse(Integer responseCode, JsonError error, T object) {
        this.responseCode = responseCode;
        this.error = error;
        this.data = object;
        this.serverTime = System.currentTimeMillis();
    }

    public JsonResponse(Integer responseCode, T data) {
        this.responseCode = responseCode;
        this.data = data;
    }

    public JsonResponse() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getServerTime() {
        return serverTime;
    }

    public void setServerTime(Long serverTime) {
        this.serverTime = serverTime;
    }
    
    

    public JsonError getError() {
        return error;
    }

    public void setError(JsonError error) {
        this.error = error;
    }

    public T getObject() {
        return data;
    }

    public void setObject(T object) {
        this.data = object;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }
}
