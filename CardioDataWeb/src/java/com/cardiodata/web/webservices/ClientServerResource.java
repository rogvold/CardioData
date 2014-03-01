package com.cardiodata.web.webservices;

import com.cardiodata.core.jpa.ClientServer;
import com.cardiodata.exceptions.CardioDataException;
import com.cardiodata.json.CardioDataExceptionWrapper;
import com.cardiodata.json.JsonResponse;
import com.cardiodata.json.ResponseConstants;
import com.cardiodata.json.SimpleResponseWrapper;
import com.cardiodata.managers.ClientServerManagerLocal;
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
@Path("clientServer")
@Stateless
public class ClientServerResource {

    @Context
    private UriInfo context;
    @EJB
    ClientServerManagerLocal csMan;

    /**
     * Creates a new instance of ClientServerResource
     */
    public ClientServerResource() {
    }

    @GET
    @Produces("application/json")
    @Path("getServer")
    public String getClientServer(@QueryParam("serverId") Long serverId) {
        try {
            if (serverId == null) {
                throw new CardioDataException("serverId is not defined");
            }
            ClientServer c = csMan.getClientServerById(serverId);
            JsonResponse<ClientServer> jr = new JsonResponse<ClientServer>(ResponseConstants.OK, c);
            return SimpleResponseWrapper.getJsonResponse(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json")
    @Path("createClientServer")
    public String createClientServer(@FormParam("name") String name) {
        try {
            ClientServer c = csMan.createClientServer(name);
            JsonResponse<ClientServer> jr = new JsonResponse<ClientServer>(ResponseConstants.OK, c);
            return SimpleResponseWrapper.getJsonResponse(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapException(e);
        }
    }

    @POST
    @Produces("application/json")
    @Path("updateClientServer")
    public String updateClientServer(@FormParam("serverId") Long serverId, @FormParam("name") String name) {
        try {
            if (serverId == null) {
                throw new CardioDataException("server id is null");
            }
            ClientServer c = csMan.updateClientServer(serverId, name);
            JsonResponse<ClientServer> jr = new JsonResponse<ClientServer>(ResponseConstants.OK, c);
            return SimpleResponseWrapper.getJsonResponse(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapException(e);
        }
    }
    
    
    
}
