package com.cardiodata.web.webservices;

import com.cardiodata.core.jpa.ApiToken;
import com.cardiodata.core.jpa.User;
import com.cardiodata.core.jpa.UserAccount;
import com.cardiodata.enums.AccountTypeEnum;
import com.cardiodata.exceptions.CardioDataException;
import com.cardiodata.json.*;
import com.cardiodata.managers.CardioSessionManagerLocal;
import com.cardiodata.managers.ClientServerManagerLocal;
import com.cardiodata.managers.UserManagerLocal;
import com.cardiodata.utils.StringUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
@Path("auth")
@Stateless
public class AuthResource {

    public static final String FACEBOOK_APP_ID = "788471417852291";
    public static final String FACEBOOK_APP_SECRET = "4b63cf6bfcb8d19736f17e08c95064b1";
    
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
    @Path("createUserAccount")
    public Response createUserAccount(@FormParam("token") String token, @FormParam("userId") Long userId, @FormParam("login") String login,  @FormParam("type") AccountTypeEnum type) {
        try {
            tokenMan.assertToken(userId, token);
            UserAccount acc = userMan.createUserAccount(type, userId, login, null);
            JsonResponse<UserAccount> jr = new JsonResponse<UserAccount>(ResponseConstants.OK, acc);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }
    
    @POST
    @Produces("application/json")
    @Path("changePassword")
    public Response changePassword(@FormParam("token") String token, @FormParam("userId") Long userId,  @FormParam("type") AccountTypeEnum type, @FormParam("newPassword") String newPassword) {
        try {
            tokenMan.assertToken(userId, token);
            UserAccount acc = userMan.changePassword(userId, type, newPassword);
            JsonResponse<UserAccount> jr = new JsonResponse<UserAccount>(ResponseConstants.OK, acc);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }
    
    
    
    @POST
    @Produces("application/json")
    @Path("lazyFacebookAuth")
    public Response lazyFacebookAuth(@FormParam("token") String token, @FormParam("fbToken") String fbToken, @FormParam("userId") Long userId,  @FormParam("fbId") String fbId) throws SignatureException, UnsupportedEncodingException, IOException {
        try {
            tokenMan.assertToken(userId, token);

            System.out.println("lazyFacebookAuth occured:");
            System.out.println("fbId = " + fbId + " ; fbToken = " + fbToken);
            String proof = StringUtils.hashMac(fbToken, FACEBOOK_APP_SECRET);
            String fbResp = makeFacebookProofPostRequest(fbToken, proof);
            System.out.println("fbResponse = " + fbResp);
            //todo: find out what is the fbResb in logs
            
            UserAccount acc = userMan.createUserAccount(AccountTypeEnum.FACEBOOK, userId, fbId, null);
            JsonResponse<UserAccount> jr = new JsonResponse<UserAccount>(ResponseConstants.OK, acc);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }

    @POST
    @Produces("application/json")
    @Path("lazyFacebookLogin")
    public Response lazyFacebookLogin(@FormParam("fbToken") String fbToken, @FormParam("fbId") String fbId, @FormParam("email") String email, @FormParam("password") String password, @FormParam("firstName") String firstName, @FormParam("lastName") String lastName) throws SignatureException, UnsupportedEncodingException, IOException {
        try {
            String proof = StringUtils.hashMac(fbToken, FACEBOOK_APP_SECRET);
            String fbResp = makeFacebookProofPostRequest(fbToken, proof);
            //check
            User u = userMan.getLazyUserByEmailAndPassword(email, password, firstName, lastName);
            UserAccount acc = userMan.getLazyUserAccount(AccountTypeEnum.FACEBOOK, u.getId(), fbId, null);
            ApiToken token = tokenMan.getCurrentToken(u.getId());
            JsonResponse<ApiToken> jr = new JsonResponse<ApiToken>(ResponseConstants.OK, token);
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
    @Path("getUserByToken")
    public Response getUserByToken(@FormParam("token") String token) {
        try {
            if (token == null) {
                throw new CardioDataException("token is not specified");
            }
            User u = userMan.getUserByToken(token);
            JsonResponse<User> jr = new JsonResponse<User>(ResponseConstants.OK, u);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }
    
    @POST
    @Produces("application/json")
    @Path("getUserById")
    public Response getUserById(@FormParam("token") String token, @FormParam("userId") Long userId) {
        try {
            if (token == null) {
                throw new CardioDataException("token is not specified");
            }
            tokenMan.assertToken(userId, token);
            User u = userMan.getUserById(userId);
            JsonResponse<User> jr = new JsonResponse<User>(ResponseConstants.OK, u);
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


    @POST
    @Produces("application/json")
    @Path("updateUserProfile")
    public Response updateUserProfile(@FormParam("token") String token, @FormParam("serializedUser") String serializedUser) {
        try {
            userMan.updateUserProfile(token, serializedUser);
            JsonResponse<String> jr = new JsonResponse<String>(ResponseConstants.OK, ResponseConstants.YES);
            return SimpleResponseWrapper.getJsonResponseCORS(jr);
        } catch (CardioDataException e) {
            return CardioDataExceptionWrapper.wrapExceptionCORS(e);
        }
    }

    private String makeFacebookProofPostRequest(String accessToken, String proof) throws MalformedURLException, UnsupportedEncodingException, IOException{
        HttpClient client = new DefaultHttpClient();
	HttpPost post = new HttpPost("https://graph.facebook.com");
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
	urlParameters.add(new BasicNameValuePair("access_token", accessToken));
	urlParameters.add(new BasicNameValuePair("appsecret_proof", proof));
	urlParameters.add(new BasicNameValuePair("batch", "[{\"method\":\"GET\", \"relative_url\":\"me\"}]"));
        
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
 
	HttpResponse response = client.execute(post);
        
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
 
	StringBuffer result = new StringBuffer();
	String line = "";
	while ((line = rd.readLine()) != null) {
			result.append(line);
	}
        return line;
      }
    
    
    /**
     * Creates a new instance of AuthResource
     */
    public AuthResource() {
    }
}
