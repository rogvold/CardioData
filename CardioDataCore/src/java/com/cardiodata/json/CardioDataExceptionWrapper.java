package com.cardiodata.json;

import com.cardiodata.exceptions.CardioDataException;
import com.cardiodata.utils.CORSUtils;
import com.google.gson.Gson;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
public class CardioDataExceptionWrapper {

    public static String wrapException(CardioDataException exc) {
        return (new Gson()).toJson(new JsonResponse(ResponseConstants.ERROR, new JsonError(exc.getMessage(), exc.getErrorCode()), null));
    }

    public static Response wrapExceptionCORS(CardioDataException exc) {
        String resp = (new Gson()).toJson(new JsonResponse(ResponseConstants.ERROR, new JsonError(exc.getMessage(), exc.getErrorCode()), null));
        return CORSUtils.makeCORS(Response.ok(resp, MediaType.APPLICATION_JSON));
    }
}
