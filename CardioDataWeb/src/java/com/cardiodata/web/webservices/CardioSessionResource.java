package com.cardiodata.web.webservices;

import com.cardiodata.core.jpa.CardioSession;
import com.cardiodata.core.jpa.ClientServer;
import com.cardiodata.exceptions.CardioDataException;
import com.cardiodata.json.*;
import com.cardiodata.managers.CardioSessionManagerLocal;
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

    @POST
    @Produces("application/json")
    @Path("createCardioSession")
    public String createCardioSession(@FormParam("userId") Long userId, @FormParam("serverId") Long serverId) {
        try {
            CardioSession cs = cardMan.createCardioSession(userId, serverId);
            JsonResponse<CardioSession> jr = new JsonResponse<CardioSession>(ResponseConstants.OK, cs);
            return SimpleResponseWrapper.getJsonResponse(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json")
    @Path("updateCardioSessionInfo")
    public String updateCardioSessionInfo(@FormParam("sessionId") Long sessionId, @FormParam("name") String name, @FormParam("description") String description) {
        try {
            CardioSession cs = cardMan.updateCardioSession(sessionId, name, description);
            JsonResponse<CardioSession> jr = new JsonResponse<CardioSession>(ResponseConstants.OK, cs);
            return SimpleResponseWrapper.getJsonResponse(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json")
    @Path("getCardioSessionData")
    public String getCardioSessionData(@FormParam("sessionId") Long sessionId) {
        try {
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
