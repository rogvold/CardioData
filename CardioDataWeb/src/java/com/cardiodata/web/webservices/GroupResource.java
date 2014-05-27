package com.cardiodata.web.webservices;

import com.cardiodata.core.jpa.User;
import com.cardiodata.enums.AccountTypeEnum;
import com.cardiodata.exceptions.CardioDataException;
import com.cardiodata.json.CardioDataExceptionWrapper;
import com.cardiodata.json.JsonResponse;
import com.cardiodata.json.ResponseConstants;
import com.cardiodata.json.SimpleResponseWrapper;
import com.cardiodata.json.TokenManagerLocal;
import com.cardiodata.managers.CardioSessionManagerLocal;
import com.cardiodata.managers.ClientServerManagerLocal;
import com.cardiodata.managers.UserGroupManagerLocal;
import com.cardiodata.managers.UserManagerLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * REST Web Service
 *
 * @author sabir
 */
@Path("group")
@Stateless
public class GroupResource {

    @Context
    private UriInfo context;

    @EJB
    ClientServerManagerLocal csMan;
    @EJB
    UserManagerLocal userMan;
    @EJB
    TokenManagerLocal tokenMan;
    @EJB
    UserGroupManagerLocal ugMan;
    
    /**
     * Creates a new instance of GroupResource
     */
    public GroupResource() {
    }

    @POST
    @Produces("application/json")
    @Path("getTraineesOfTrainer")
    public Response getTraineesOfTrainer(@FormParam("token") String token, @FormParam("trainerId") Long trainerId) {
        try {
            if (trainerId == null) {
                throw new CardioDataException("trainerId is not defined");
            }
            tokenMan.assertToken(trainerId, token);
            List<User> list = userMan.getTrainees(trainerId);
            JsonResponse<List<User>> jr = new JsonResponse<List<User>>(ResponseConstants.OK, list);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }   
    
    
    @POST
    @Produces("application/json")
    @Path("inviteTrainee")
    public Response inviteTrainee(@FormParam("token") String token, @FormParam("trainerId") Long trainerId, @FormParam("traineeId") Long traineeId) {
        try {
            if (trainerId == null) {
                throw new CardioDataException("trainerId is not defined");
            }
            tokenMan.assertToken(trainerId, token);
            ugMan.inviteTrainee(trainerId, traineeId);
            JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.OK, ResponseConstants.YES);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }
    
    @POST
    @Produces("application/json")
    @Path("removeTraineeInvitation")
    public Response removeTraineeInvitation(@FormParam("token") String token, @FormParam("trainerId") Long trainerId, @FormParam("traineeId") Long traineeId) {
        try {
            if (trainerId == null) {
                throw new CardioDataException("trainerId is not defined");
            }
            tokenMan.assertToken(trainerId, token);
            ugMan.removeTraineeInvitation(trainerId, traineeId);
            JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.OK, ResponseConstants.YES);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }
    
    
    
}
