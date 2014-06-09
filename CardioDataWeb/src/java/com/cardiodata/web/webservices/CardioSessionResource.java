package com.cardiodata.web.webservices;

import com.cardiodata.core.jpa.CardioMoodSession;
import com.cardiodata.exceptions.CardioDataException;
import com.cardiodata.json.*;
import com.cardiodata.json.entity.JsonRRInterval;
import com.cardiodata.managers.CardioSessionManagerLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * REST Web Service
 *
 * @author rogvold
 * 
 * 
 * api v1
 */
@Path("cardioSession")
@Stateless
public class CardioSessionResource {

    @Context
    private UriInfo context;
    @EJB
    CardioSessionManagerLocal cardMan;
    @EJB
    TokenManagerLocal tokenMan;

    //tested
    @POST
    @Produces("application/json")
    @Path("createCardioSession")
    public Response createCardioSession(@FormParam("token") String token, @FormParam("userId") Long userId, @FormParam("serverId") Long serverId, @FormParam("creationTimestamp") Long creationTimestamp) {
        try {
            tokenMan.assertToken(userId, token);
            CardioMoodSession cs = cardMan.createCardioSession(userId, serverId, "JsonRRInterval", creationTimestamp);
            JsonResponse<CardioMoodSession> jr = new JsonResponse<CardioMoodSession>(ResponseConstants.OK, cs);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }

    //tested
    @POST
    @Produces("application/json")
    @Path("getCardioSessionsOfUser")
    public Response getCardioSessionsOfUser(@FormParam("token") String token, @FormParam("userId") Long userId, @FormParam("serverId") Long serverId) {
        try {
            tokenMan.assertToken(userId, token);
            List<CardioMoodSession> list = cardMan.getCardioSessionsOfUser(userId, serverId, JsonRRInterval.class.getSimpleName());
            JsonResponse<List<CardioMoodSession>> jr = new JsonResponse<List<CardioMoodSession>>(ResponseConstants.OK, list);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }

    //tested
    @POST
    @Produces("application/json")
    @Path("deleteCardioSession")
    public Response deleteCardioSession(@FormParam("token") String token, @FormParam("userId") Long userId, @FormParam("sessionId") Long sessionId) {
        try {
            tokenMan.assertToken(userId, token);
            cardMan.deleteCardioSession(sessionId);
            JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.OK, ResponseConstants.YES);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }

    //tested
    @POST
    @Produces("application/json")
    @Path("appendDataToCardioSession")
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
    @Path("rewriteCardioSessionData")
    public Response rewriteCardioSessionData(@FormParam("token") String token, @FormParam("userId") Long userId, @FormParam("serializedData") String serializedData) {
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
    @Path("updateCardioSessionInfo")
    public Response updateCardioSessionInfo(@FormParam("token") String token, @FormParam("userId") Long userId, @FormParam("sessionId") Long sessionId, @FormParam("name") String name, @FormParam("description") String description) {
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
    @Path("getCardioSessionData")
    public Response getCardioSessionData(@FormParam("token") String token, @FormParam("userId") Long userId, @FormParam("sessionId") Long sessionId) {
        try {
            tokenMan.assertToken(userId, token);
            CardioSessionWithData cswd = cardMan.getCardioSessionWihData(sessionId, null);
            JsonResponse<CardioSessionWithData> jr = new JsonResponse<CardioSessionWithData>(ResponseConstants.OK, cswd);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }

    @POST
    @Produces("application/json")
    @Path("renameCardioSession")
    public Response renameCardioSession(@FormParam("token") String token, @FormParam("userId") Long userId,  @FormParam("sessionId") Long sessionId, @FormParam("name") String newName) {
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
     * Creates a new instance of CardioSessionResource
     */
    public CardioSessionResource() {
    }
}
