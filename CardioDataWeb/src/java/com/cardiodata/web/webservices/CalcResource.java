package com.cardiodata.web.webservices;

import com.cardiodata.additional.CalcInputData;
import com.cardiodata.additional.CalcManager;
import com.cardiodata.exceptions.CardioDataException;
import com.cardiodata.json.*;
import com.google.gson.Gson;
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
public class CalcResource {

    @Context
    private UriInfo context;

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
                res = CalcManager.getTensionArray(rrs, time);
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
                res = CalcManager.getSDNN(rrs, time);
            }

            JsonResponse<double[][]> jr = new JsonResponse<double[][]>(ResponseConstants.OK, res);
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
