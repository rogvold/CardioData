package com.cardiodata.json;

import com.englishsmacks.exceptions.SmackException;
import com.google.gson.Gson;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
public class SmackExceptionWrapper {

    public static String wrapException(SmackException exc) {
        return (new Gson()).toJson(new JsonResponse(ResponseConstants.ERROR, new JsonError(exc.getMessage(), exc.getErrorCode()), null));
    }
}