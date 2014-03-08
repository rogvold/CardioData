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

    @GET
    @Produces("application/json")
    @Path("test")
    public String test() {
//        try {
//            System.out.println("registerUserByEmailAndPassword:");
//            System.out.println("email = " + email);
//            System.out.println("password = " + password);
//            if (email == null || password == null) {
//                throw new CardioDataException("email or password is not specified");
//            }
        String s = "test";
//            User u = userMan.registerUser(AccountTypeEnum.EMAIL, email, password);
        JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.OK, s);
        return SimpleResponseWrapper.getJsonResponse(jr);
//        } catch (CardioDataException e) {
//            return CardioDataExceptionWrapper.wrapException(e);
//        }
    }

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("registerUserByEmailAndPassword")
    public String registerUserByEmailAndPassword(@FormParam("email") String email, @FormParam("password") String password) {
        try {
            System.out.println("registerUserByEmailAndPassword:");
            System.out.println("email = " + email);
            System.out.println("password = " + password);
            if (email == null || password == null) {
                throw new CardioDataException("email or password is not specified");
            }
            User u = userMan.registerUser(AccountTypeEnum.EMAIL, email, password);
            JsonResponse<User> jr = new JsonResponse<User>(ResponseConstants.OK, u);
            return SimpleResponseWrapper.getJsonResponse(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapException(e);
        }
    }

//    @POST
//    @Produces("application/json")
//    @Consumes("application/json")
//    @Path("getUserToken")
//    public String getUserToken(@FormParam("userId") Long userId) {
//        try {
//            if (userId == null) {
//                throw new CardioDataException("userId is null");
//            }
//            ApiToken token = tokenMan.getCurrentToken(userId);
//            JsonResponse<ApiToken> jr = new JsonResponse<ApiToken>(ResponseConstants.OK, token);
//            return SimpleResponseWrapper.getJsonResponse(jr);
//        } catch (CardioDataException e) {
//            return CardioDataExceptionWrapper.wrapException(e);
//        }
//    }
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("loginByEmailAndPassword")
    public String loginByEmailAndPassword(@FormParam("email") String email, @FormParam("password") String password) {
        try {
            if (email == null || password == null) {
                throw new CardioDataException("email or password is not specified");
            }
            User u = userMan.loginUser(AccountTypeEnum.EMAIL, email, password);
            ApiToken token = tokenMan.getCurrentToken(u.getId());
            JsonResponse<ApiToken> jr = new JsonResponse<ApiToken>(ResponseConstants.OK, token);
            return SimpleResponseWrapper.getJsonResponse(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapException(e);
        }
    }

    /**
     * Creates a new instance of AuthResource
     */
    public AuthResource() {
    }
}
