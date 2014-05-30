package com.cardiodata.web.webservices;

import com.cardiodata.core.jpa.CardioMoodSession;
import com.cardiodata.exceptions.CardioDataException;
import com.cardiodata.json.CardioDataExceptionWrapper;
import com.cardiodata.json.CardioSessionWithData;
import com.cardiodata.json.JsonResponse;
import com.cardiodata.json.ResponseConstants;
import com.cardiodata.json.SimpleResponseWrapper;
import com.cardiodata.json.TokenManagerLocal;
import com.cardiodata.managers.CardioSessionManagerLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * REST Web Service
 *
 * @author sabir
 * 
 * api v2
 */
@Path("v2/CardioMoodSession")
@Stateless
public class CardioMoodSessionResource {

     @Context
    private UriInfo context;
    @EJB
    CardioSessionManagerLocal cardMan;
    @EJB
    TokenManagerLocal tokenMan;


    /*
    className = JsonRRInterval | JsonGPS
    */
    @POST
    @Produces("application/json")
    @Path("createCardioMoodSession")
    public Response createMoodCardioSession(@FormParam("token") String token, @FormParam("userId") Long userId, @FormParam("serverId") Long serverId, @FormParam("className") String className) {
        try {
            tokenMan.assertToken(userId, token);
            if (className == null || "".equals(className)){
                throw new CardioDataException("className is not specified");
            }
            CardioMoodSession cs = cardMan.createCardioSession(userId, serverId, className);
            JsonResponse<CardioMoodSession> jr = new JsonResponse<CardioMoodSession>(ResponseConstants.OK, cs);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }


    @POST
    @Produces("application/json")
    @Path("getCardioMoodSessionsOfUser")
    public Response getCardioMoodSessionsOfUser(@FormParam("token") String token, @FormParam("userId") Long userId, @FormParam("serverId") Long serverId) {
        try {
            tokenMan.assertToken(userId, token);
            List<CardioMoodSession> list = cardMan.getCardioSessionsOfUser(userId, serverId);
            JsonResponse<List<CardioMoodSession>> jr = new JsonResponse<List<CardioMoodSession>>(ResponseConstants.OK, list);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }

    
    @POST
    @Produces("application/json")
    @Path("deleteCardioMoodSession")
    public Response deleteCardioMoodSession(@FormParam("token") String token, @FormParam("userId") Long userId, @FormParam("sessionId") Long sessionId) {
        try {
            tokenMan.assertToken(userId, token);
            cardMan.deleteCardioSession(sessionId);
            JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.OK, ResponseConstants.YES);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }

    
    @POST
    @Produces("application/json")
    @Path("appendDataToCardioMoodSession")
    public Response appendDataToCardioSession(@FormParam("token") String token, @FormParam("userId") Long userId, @FormParam("serializedData") String serializedData) {
        try {
            tokenMan.assertToken(userId, token);
            cardMan.saveCardioSessionData(serializedData);
            JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.OK, ResponseConstants.YES);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }

    @POST
    @Produces("application/json")
    @Path("rewriteCardioMoodSessionData")
    public Response rewriteCardioMoodSessionData(@FormParam("token") String token, @FormParam("userId") Long userId, @FormParam("serializedData") String serializedData) {
        try {
            tokenMan.assertToken(userId, token);
            cardMan.rewriteCardioSessionData(serializedData);
            JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.OK, ResponseConstants.YES);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }

    @POST
    @Produces("application/json")
    @Path("updateCardioMoodSessionInfo")
    public Response updateCardioMoodSessionInfo(@FormParam("token") String token, @FormParam("userId") Long userId, @FormParam("sessionId") Long sessionId, @FormParam("name") String name, @FormParam("description") String description) {
        try {
            tokenMan.assertToken(userId, token);
            CardioMoodSession cs = cardMan.updateCardioSession(sessionId, name, description);
            JsonResponse<CardioMoodSession> jr = new JsonResponse<CardioMoodSession>(ResponseConstants.OK, cs);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }

    @POST
    @Produces("application/json")
    @Path("getCardioMoodSessionData")
    public Response getCardioMoodSessionData(@FormParam("token") String token, @FormParam("userId") Long userId, @FormParam("sessionId") Long sessionId) {
        try {
            tokenMan.assertToken(userId, token);
            CardioSessionWithData cswd = cardMan.getCardioSessionWihData(sessionId);
            JsonResponse<CardioSessionWithData> jr = new JsonResponse<CardioSessionWithData>(ResponseConstants.OK, cswd);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }

    @POST
    @Produces("application/json")
    @Path("renameMoodCardioSession")
    public Response renameCardioMoodSession(@FormParam("token") String token, @FormParam("userId") Long userId,  @FormParam("sessionId") Long sessionId, @FormParam("name") String newName) {
        try {
            tokenMan.assertToken(userId, token);
            CardioMoodSession cs = cardMan.renameCardioSession(sessionId, newName);
            JsonResponse<CardioMoodSession> jr = new JsonResponse<CardioMoodSession>(ResponseConstants.OK, cs);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }

    /**
     * Creates a new instance of CardioMoodSessionResource
     */
    public CardioMoodSessionResource() {
    }

}
