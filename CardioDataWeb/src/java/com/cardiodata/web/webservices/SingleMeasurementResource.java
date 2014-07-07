
package com.cardiodata.web.webservices;

import com.cardiodata.core.jpa.CardioMoodSingleMeasurement;
import com.cardiodata.exceptions.CardioDataException;
import com.cardiodata.json.CardioDataExceptionWrapper;
import com.cardiodata.json.JsonResponse;
import com.cardiodata.json.ResponseConstants;
import com.cardiodata.json.SimpleResponseWrapper;
import com.cardiodata.json.TokenManagerLocal;
import com.cardiodata.managers.SingleMeasurementManagerLocal;
import com.google.gson.Gson;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * REST Web Service
 *
 * @author sabir
 */
@Path("v2/SingleMeasurement")
@Stateless
public class SingleMeasurementResource {

    @Context
    private UriInfo context;

    @EJB
    SingleMeasurementManagerLocal sMan;
    
    @EJB
    TokenManagerLocal tokenMan;
    
    @POST
    @Produces("application/json")
    @Path("rewriteSingleMeasurement")
    public Response rewriteSingleMeasurement(@FormParam("token") String token, 
            @FormParam("userId") Long userId, @FormParam("serializedData") String serializedData) {
        try {
            tokenMan.assertToken(userId, token);
            CardioMoodSingleMeasurement me = null;
            try {
                me = (new Gson()).fromJson(serializedData, CardioMoodSingleMeasurement.class);
                me = sMan.updateSingleMeasurement(me);
            } catch (Exception e) {
                throw new CardioDataException(e.getMessage());
            }
            JsonResponse<CardioMoodSingleMeasurement> jr = new JsonResponse<CardioMoodSingleMeasurement>(ResponseConstants.OK, me);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }

    /**
     * 
     * @param token
     * @param userId
     * @param serverId - optional
     * @param dataType - optional
     * @return 
     */
    @POST
    @Produces("application/json")
    @Path("getSingleMeasurementsOfUser")
    public Response getSingleMeasurementsOfUser(@FormParam("token") String token, 
            @FormParam("userId") Long userId, @FormParam("serverId") Long serverId, @FormParam("dataType") String dataType) {
        try {
            tokenMan.assertToken(userId, token);
            List<CardioMoodSingleMeasurement> list = sMan.getMeasurementsOfUser(userId, serverId, CardioMoodSingleMeasurement.SingleMeasurementDataType.valueOf(dataType));
            JsonResponse<List<CardioMoodSingleMeasurement>> jr = new JsonResponse<List<CardioMoodSingleMeasurement>>(ResponseConstants.OK, list);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }
    
    @POST
    @Produces("application/json")
    @Path("getLastSingleMeasurementOfUser")
    public Response getLastSingleMeasurementOfUser(@FormParam("token") String token, 
            @FormParam("userId") Long userId, @FormParam("serverId") Long serverId, @FormParam("dataType") String dataType) {
        try {
            tokenMan.assertToken(userId, token);
            CardioMoodSingleMeasurement c = sMan.getLastSingleMeasurement(userId, serverId, CardioMoodSingleMeasurement.SingleMeasurementDataType.valueOf(dataType));
            JsonResponse<CardioMoodSingleMeasurement> jr = new JsonResponse<CardioMoodSingleMeasurement>(ResponseConstants.OK, c);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }
    
    @POST
    @Produces("application/json")
    @Path("deleteSingleMeasurement")
    public Response deleteSingleMeasurement(@FormParam("token") String token, 
            @FormParam("userId") Long userId, @FormParam("measurementId") Long mId) {
        try {
            tokenMan.assertToken(userId, token);
            sMan.deleteSingleMeasurement(mId);
            JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.OK, ResponseConstants.YES);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }
    
    @POST
    @Produces("application/json")
    @Path("renameSingleMeasurement")
    public Response renameSingleMeasurement(@FormParam("token") String token, 
            @FormParam("userId") Long userId, @FormParam("measurementId") Long mId, 
            @FormParam("newName") String newName, @FormParam("newDescription") String newDescription) {
        try {
            tokenMan.assertToken(userId, token);
            CardioMoodSingleMeasurement m = sMan.renameSingleMeasurement(mId, newName, newDescription);
            JsonResponse<CardioMoodSingleMeasurement> jr = new JsonResponse<CardioMoodSingleMeasurement>(ResponseConstants.OK, m);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }
    
    /**
     * Creates a new instance of SingleMeasurementResource
     */
    public SingleMeasurementResource() {
    }

    /**
     * Retrieves representation of an instance of com.cardiodata.web.webservices.SingleMeasurementResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of SingleMeasurementResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
