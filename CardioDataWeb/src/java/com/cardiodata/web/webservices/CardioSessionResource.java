package com.cardiodata.web.webservices;

import com.cardiodata.core.jpa.CardioSession;
import com.cardiodata.exceptions.CardioDataException;
import com.cardiodata.json.*;
import com.cardiodata.managers.CardioSessionManagerLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 * REST Web Service
 *
 * @author rogvold
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
    @Consumes("application/json")
    @Path("createCardioSession")
    public String createCardioSession(@FormParam("token") String token, @FormParam("userId") Long userId, @FormParam("serverId") Long serverId) {
        try {
            tokenMan.assertToken(userId, token);
            CardioSession cs = cardMan.createCardioSession(userId, serverId);
            JsonResponse<CardioSession> jr = new JsonResponse<CardioSession>(ResponseConstants.OK, cs);
            return SimpleResponseWrapper.getJsonResponse(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapException(e);
        }
    }

    //tested
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("getCardioSessionsOfUser")
    public String getCardioSessionsOfUser(@FormParam("token") String token, @FormParam("userId") Long userId, @FormParam("serverId") Long serverId) {
        try {
            tokenMan.assertToken(userId, token);
            List<CardioSession> list = cardMan.getCardioSessionsOfUser(userId, serverId);
            JsonResponse<List<CardioSession>> jr = new JsonResponse<List<CardioSession>>(ResponseConstants.OK, list);
            return SimpleResponseWrapper.getJsonResponse(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapException(e);
        }
    }

    //tested
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("deleteCardioSession")
    public String deleteCardioSession(@FormParam("token") String token, @FormParam("userId") Long userId, @FormParam("sessionId") Long sessionId) {
        try {
            tokenMan.assertToken(userId, token);
            cardMan.deleteCardioSession(sessionId);
            JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.OK, ResponseConstants.YES);
            return SimpleResponseWrapper.getJsonResponse(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("updateCardioSessionInfo")
    public String updateCardioSessionInfo(@FormParam("token") String token, @FormParam("userId") Long userId, @FormParam("sessionId") Long sessionId, @FormParam("name") String name, @FormParam("description") String description) {
        try {
            tokenMan.assertToken(userId, token);
            CardioSession cs = cardMan.updateCardioSession(sessionId, name, description);
            JsonResponse<CardioSession> jr = new JsonResponse<CardioSession>(ResponseConstants.OK, cs);
            return SimpleResponseWrapper.getJsonResponse(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("getCardioSessionData")
    public String getCardioSessionData(@FormParam("token") String token, @FormParam("userId") Long userId, @FormParam("sessionId") Long sessionId) {
        try {
            tokenMan.assertToken(userId, token);
            CardioSessionWithData cswd = cardMan.getCardioSessionWihData(sessionId);
            JsonResponse<CardioSessionWithData> jr = new JsonResponse<CardioSessionWithData>(ResponseConstants.OK, cswd);
            return SimpleResponseWrapper.getJsonResponse(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapException(e);
        }
    }

    /**
     * Creates a new instance of CardioSessionResource
     */
    public CardioSessionResource() {
    }
}
