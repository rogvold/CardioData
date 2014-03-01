package com.cardiodata.json;

import com.cardiodata.exceptions.CardioDataException;
import com.google.gson.Gson;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
public class CardioDataExceptionWrapper {

    public static String wrapException(CardioDataException exc) {
        return (new Gson()).toJson(new JsonResponse(ResponseConstants.ERROR, new JsonError(exc.getMessage(), exc.getErrorCode()), null));
    }
    
}
