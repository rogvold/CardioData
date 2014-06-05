package com.cardiodata.web.webservices;

import com.cardiodata.additional.CalcInputData;
import com.cardiodata.utils.CalcManager;
import com.cardiodata.core.jpa.CardioMoodSession;
import com.cardiodata.exceptions.CardioDataException;
import com.cardiodata.json.*;
import com.cardiodata.managers.CardioSessionManagerLocal;
import com.google.gson.Gson;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * REST Web Service
 *
 * @author rogvold
 */
@Path("calc")
@Stateless
public class CalcResource {

    @Context
    private UriInfo context;
    
    @EJB
    CardioSessionManagerLocal csMan;
    
    @EJB
    TokenManagerLocal tokenMan;

    /**
     * Creates a new instance of CalcResource
     */
    public CalcResource() {
    }

    /**
     * Retrieves representation of an instance of
     * com.cardiodata.web.webservices.CalcResource
     *
     * @return an instance of java.lang.String
     */
    @POST
    @Produces("application/json")
    @Path("getSpectrum")
    public Response getSpectrum(@FormParam("data") String data) {
        try {
            System.out.println(data);
            if (data == null) {
                throw new CardioDataException("data is not specified");
            }
            CalcInputData d = (new Gson()).fromJson(data, CalcInputData.class);
            double[][] res = CalcManager.getSpectrum(d.getSeries());

            JsonResponse<double[][]> jr = new JsonResponse<double[][]>(ResponseConstants.OK, res);

            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }

    @POST
    @Produces("application/json")
    @Path("getTension")
    public Response getTension(@FormParam("data") String data) {
        try {
            System.out.println(data);
            if (data == null) {
                throw new CardioDataException("data is not specified");
            }
            CalcInputData d = (new Gson()).fromJson(data, CalcInputData.class);

            double[] rrs = d.getSeries();
            double[] time = d.getTime();

            double[][] res;

            if (time == null) {
                res = CalcManager.getTensionArray(d.getSeries());
            } else {
                res = CalcManager.getTensionArray(rrs, time, true);
            }

            JsonResponse<double[][]> jr = new JsonResponse<double[][]>(ResponseConstants.OK, res);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }

    @POST
    @Produces("application/json")
    @Path("getSDNN")
    public Response getSDNN(@FormParam("data") String data) {
        try {
            System.out.println(data);
            if (data == null) {
                throw new CardioDataException("data is not specified");
            }
            CalcInputData d = (new Gson()).fromJson(data, CalcInputData.class);

            double[] rrs = d.getSeries();
            double[] time = d.getTime();
            double[][] res;

            if (time == null) {
                res = CalcManager.getSDNN(d.getSeries());
            } else {
                res = CalcManager.getSDNN(rrs, time, true);
            }

            JsonResponse<double[][]> jr = new JsonResponse<double[][]>(ResponseConstants.OK, res);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }

       
    @POST
    @Produces("application/json")
    @Path("freshSessionId")
    public Response getFreshSessionId(@FormParam("userId") Long userId) {
        try {
            if (userId == null){
                throw new CardioDataException("userId is null");
            }
            Long sessionId = csMan.getTheMostFreshCardioMoodSessionIdOfUser(userId);
            JsonResponse<Long> jr = new JsonResponse<Long>(ResponseConstants.OK, sessionId);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }
    
           
    @POST
    @Produces("application/json")
    @Path("getCalculatedSession")
    public Response getCalculatedSession(@FormParam("sessionId") Long sessionId) {
        try {
            if (sessionId == null){
                throw new CardioDataException("sessionId is null");
            }
            CalculatedRRSession rSession = csMan.getCalculatedRRSession(sessionId, true);
            JsonResponse<CalculatedRRSession> jr = new JsonResponse<CalculatedRRSession>(ResponseConstants.OK, rSession);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }
    
    @POST
    @Produces("application/json")
    @Path("test")
    public Response testCalc(@FormParam("token") String token, @FormParam("trainerId") Long trainerId) {
        try {
            tokenMan.assertToken(trainerId, token);
            JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.OK, ResponseConstants.YES);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }
    
    @POST
    @Produces("application/json")
    @Path("test2")
    public Response test2Calc(@FormParam("token") String token, @FormParam("trainerId") Long trainerId) {
        try {
            tokenMan.assertToken(trainerId, token);
            JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.OK, ResponseConstants.YES);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }
    
    /**
     * PUT method for updating or creating an instance of CalcResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
