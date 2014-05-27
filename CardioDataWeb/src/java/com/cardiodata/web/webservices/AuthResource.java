package com.cardiodata.web.webservices;

import com.cardiodata.core.jpa.ApiToken;
import com.cardiodata.core.jpa.User;
import com.cardiodata.enums.AccountTypeEnum;
import com.cardiodata.exceptions.CardioDataException;
import com.cardiodata.json.*;
import com.cardiodata.managers.CardioSessionManagerLocal;
import com.cardiodata.managers.ClientServerManagerLocal;
import com.cardiodata.managers.UserManagerLocal;
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
 */
@Path("auth")
@Stateless
public class AuthResource {

    @Context
    private UriInfo context;
    @EJB
    ClientServerManagerLocal csMan;
    @EJB
    UserManagerLocal userMan;
    @EJB
    CardioSessionManagerLocal cardMan;
    @EJB
    TokenManagerLocal tokenMan;

    @POST
    @Produces("application/json")
    @Path("registerUserByEmailAndPassword")
    public Response registerUserByEmailAndPassword(@FormParam("email") String email, @FormParam("password") String password) {
        try {
            System.out.println("registerUserByEmailAndPassword:");
            System.out.println("email = " + email);
            System.out.println("password = " + password);
            if (email == null || password == null) {
                throw new CardioDataException("email or password is not specified");
            }
            User u = userMan.registerUser(AccountTypeEnum.EMAIL, email, password);
            JsonResponse<User> jr = new JsonResponse<User>(ResponseConstants.OK, u);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }
    
    @POST
    @Produces("application/json")
    @Path("registerTrainerByEmailAndPassword")
    public Response registerTrainerByEmailAndPassword(@FormParam("email") String email, @FormParam("password") String password) {
        try {
            System.out.println("registerUserByEmailAndPassword:");
            System.out.println("email = " + email);
            System.out.println("password = " + password);
            if (email == null || password == null) {
                throw new CardioDataException("email or password is not specified");
            }
            User u = userMan.registerTrainer(AccountTypeEnum.EMAIL, email, password);
            JsonResponse<User> jr = new JsonResponse<User>(ResponseConstants.OK, u);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }

    @POST
    @Produces("application/json")
    @Path("loginByEmailAndPassword")
    public Response loginByEmailAndPassword(@FormParam("email") String email, @FormParam("password") String password) {
        try {
            if (email == null || password == null) {
                throw new CardioDataException("email or password is not specified");
            }
            User u = userMan.loginUser(AccountTypeEnum.EMAIL, email, password);
            ApiToken token = tokenMan.getCurrentToken(u.getId());
            JsonResponse<ApiToken> jr = new JsonResponse<ApiToken>(ResponseConstants.OK, token);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }

    @POST
    @Produces("application/json")
    @Path("updateUserInfo")
    public Response updateUserInfo(@FormParam("token") String token, @FormParam("userId") Long userId, @FormParam("firstName") String firstName, @FormParam("lastName") String lastName) {
        try {
            if (userId == null) {
                throw new CardioDataException("userId is not defined");
            }
            tokenMan.assertToken(userId, token);
            userMan.updateUserProfile(userId, firstName, lastName);
            JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.OK, ResponseConstants.YES);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }

    /**
     * Creates a new instance of AuthResource
     */
    public AuthResource() {
    }
}
