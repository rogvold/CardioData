package com.cardiodata.additional;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
public class CORSUtils {

//    private String _corsHeaders;

    public static Response makeCORS(ResponseBuilder req, String returnMethod) {
        ResponseBuilder rb = req.header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, OPTIONS");

        if (!"".equals(returnMethod)) {
            rb.header("Access-Control-Allow-Headers", returnMethod);
        }

        return rb.build();
    }
    

//    private Response makeCORS(ResponseBuilder req) {
//        return makeCORS(req, _corsHeaders);
//    }
}
