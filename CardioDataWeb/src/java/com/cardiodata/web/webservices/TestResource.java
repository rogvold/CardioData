package com.cardiodata.web.webservices;

import com.cardiodata.utils.CORSUtils;
import com.cardiodata.core.jpa.CardioSession;
import com.google.gson.Gson;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * REST Web Service
 *
 * @author rogvold
 */
@Path("test")
public class TestResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of TestResource
     */
    public TestResource() {
    }

    /**
     * Retrieves representation of an instance of
     * com.cardiodata.web.webservices.TestResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    @GET
    @Produces("application/json")
    @Path("/test")
    public Response test(@HeaderParam("Access-Control-Request-Headers") String requestH) {
        return CORSUtils.makeCORS(Response.ok(), requestH);
    }

    @POST
    @Produces("application/json")
    @Path("/test2")
    public Response test2() {
        CardioSession cs = new CardioSession(Long.MIN_VALUE, "ololo", "llll", Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE, "ololo", Long.MAX_VALUE, Long.MAX_VALUE);
        String s = (new Gson()).toJson(cs, CardioSession.class);
        return CORSUtils.makeCORS(Response.ok(s, MediaType.APPLICATION_JSON));
    }
//    @POST
//    @Produces("application/json")
//    @Path("/test2")
//    public Response test2(@HeaderParam("Access-Control-Request-Headers") String requestH) {
//        CardioSession cs = new CardioSession(Long.MIN_VALUE, "ololo", "llll", Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE, "ololo");
//        String s = (new Gson()).toJson(cs, CardioSession.class);
//        return CORSUtils.makeCORS(Response.ok(s, MediaType.APPLICATION_JSON), requestH);
//    }

    /**
     * PUT method for updating or creating an instance of TestResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
