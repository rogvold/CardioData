package com.cardiodata.json;

import com.cardiodata.utils.CORSUtils;
import com.google.gson.Gson;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
public class SimpleResponseWrapper {

    public static String getJsonResponse(JsonResponse resp) {
        return (new Gson()).toJson(resp);
    }

    public static Response getJsonResponseCORS(JsonResponse resp) {
        String r = (new Gson()).toJson(resp);
        return CORSUtils.makeCORS(Response.ok(r, MediaType.APPLICATION_JSON));
    }
}