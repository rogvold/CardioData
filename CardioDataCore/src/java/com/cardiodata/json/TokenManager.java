package com.cardiodata.json;

import com.cardiodata.core.jpa.ApiToken;
import com.cardiodata.core.jpa.User;
import com.cardiodata.enums.UserRoleEnum;
import com.cardiodata.exceptions.CardioDataException;
import com.cardiodata.managers.UserGroupManagerLocal;
import com.cardiodata.managers.UserManagerLocal;
import com.cardiodata.utils.StringUtils;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author rogvold
 */
@Stateless
public class TokenManager implements TokenManagerLocal {

    @PersistenceContext(unitName = "CardioDataCorePU")
    EntityManager em;
    
    @EJB
    UserManagerLocal userMan;
    
    @EJB
    UserGroupManagerLocal ugMan;

    private ApiToken refreshUserToken(Long userId) throws CardioDataException {
        ApiToken token = getTokenByUserId(userId);
        if (token == null) {
            Long expStamp = (new Date()).getTime() + ApiToken.EXPIRATION_TIME;
            token = new ApiToken(userId, generateApiTokenString(), expStamp);
            token = em.merge(token);
            return token;
        }
        //Long eStamp = token.getExpirationDate() + ApiToken.EXPIRATION_TIME;
        Long eStamp = (new Date()).getTime() + ApiToken.EXPIRATION_TIME;
        token.setExpirationDate(eStamp);
        return em.merge(token);
    }

    @Override
    public ApiToken getCurrentToken(Long userId) throws CardioDataException {
        if (userId == null) {
            throw new CardioDataException("userId is not specified");
        }
        ApiToken token = getTokenByUserId(userId);
        if (token == null) {
            Long expStamp = (new Date()).getTime() + ApiToken.EXPIRATION_TIME;
            token = new ApiToken(userId, generateApiTokenString(), expStamp);
            token = em.merge(token);
            return token;
        }
        Long now = (new Date()).getTime();
//        if (now - token.getExpirationDate() >= ApiToken.EXPIRATION_TIME) {
          if (now >= token.getExpirationDate()) {
            Long expStamp = (new Date()).getTime() + ApiToken.EXPIRATION_TIME;
            token.setExpirationDate(expStamp);
            token.setToken(StringUtils.generateRandomString(ApiToken.TOKEN_LENGTH));
            return em.merge(token);
        }
        return token;
    }

    @Override
    public ApiToken getTokenById(Long tokenId) throws CardioDataException {
        if (tokenId == null) {
            throw new CardioDataException("tokenId is null");
        }
        return em.find(ApiToken.class, tokenId);
    }

    @Override
    public ApiToken getTokenByUserId(Long userId) throws CardioDataException {
        if (userId == null) {
            throw new CardioDataException("userId is null");
        }
        Query q = em.createQuery("select t from ApiToken t where t.userId = :userId").setParameter("userId", userId);
        List<ApiToken> list = q.getResultList();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public boolean isExpiredApiToken(Long tokenId) throws CardioDataException {
        ApiToken token = getTokenById(tokenId);
        if (token == null) {
            throw new CardioDataException("can not find token with id = " + tokenId);
        }
        Long now = (new Date()).getTime();
        if (now - token.getExpirationDate() >= ApiToken.EXPIRATION_TIME) {
            return true;
        }
        return false;
    }

    private String generateApiTokenString() {
        return StringUtils.generateRandomString(ApiToken.TOKEN_LENGTH);
    }

    @Override
    public void assertToken(Long userId, String tokenString) throws CardioDataException {
        if (tokenString == null) {
            throw new CardioDataException("token is not specified");
        }
        ApiToken token = getTokenByUserId(userId);
        if (isExpiredApiToken(token.getId())) {
            throw new CardioDataException("token is expired", ResponseConstants.INVALID_TOKEN_CODE);
        }
        if (!tokenString.equals(token.getToken())) {
            throw new CardioDataException("token is not valid");
        }
    }

    @Override
    public boolean hasRights(Long userId, String token) throws CardioDataException {
        if (userId == null){
            throw new CardioDataException("userId is null");
        }
        if (token == null || "".equals(token)){
            throw new CardioDataException("token is not specified");
        }
        User tUser = userMan.getUserByToken(token);
        if (tUser == null){
            throw new CardioDataException("user with specified token is not found", ResponseConstants.INVALID_TOKEN_CODE);
        }
        if (tUser.getId().equals(userId)){
            return true;
        }
        if (tUser.getUserRole().equals(UserRoleEnum.TRAINER)){
            return ugMan.areConnected(tUser.getId(), userId);
        }
        return false;
    }
}
