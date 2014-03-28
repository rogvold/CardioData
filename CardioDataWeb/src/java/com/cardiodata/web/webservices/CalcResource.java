package com.cardiodata.web.webservices;

import com.cardiodata.additional.CalcInputData;
import com.cardiodata.additional.CalcManager;
import com.cardiodata.exceptions.CardioDataException;
import com.cardiodata.json.*;
import com.google.gson.Gson;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
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
    @Consumes("application/json")
    @Path("getSpectrum")
    public String getSpectrum(@FormParam("data") String data ) {
        try {
//            String data = formParams.getFirst("data");
            System.out.println(data);
            if (data == null) {
                throw new CardioDataException("data is not specified");
            }
            CalcInputData d = (new Gson()).fromJson(data, CalcInputData.class);
            double[][] res = CalcManager.getSpectrum(d.getSeries());
            
            JsonResponse<double[][]> jr = new JsonResponse<double[][]>(ResponseConstants.OK, res);
            
            return SimpleResponseWrapper.getJsonResponse(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapException(e);
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
