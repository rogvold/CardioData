package com.cardiodata.json;

import com.cardiodata.core.jpa.ApiToken;
import com.cardiodata.exceptions.CardioDataException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.cardiodata.utils.StringUtils;
import java.util.Date;

/**
 *
 * @author rogvold
 */
@Stateless
public class TokenManager implements TokenManagerLocal {

    @PersistenceContext(unitName = "CardioDataCorePU")
    EntityManager em;

    @Override
    public ApiToken refreshUserToken(Long userId) throws CardioDataException {
        ApiToken token = getTokenByUserId(userId);
        if (token == null) {
            Long expStamp = (new Date()).getTime() + ApiToken.EXPIRATION_TIME;
            token = new ApiToken(userId, generateApiTokenString(), expStamp);
            token = em.merge(token);
            return token;
        }
        Long eStamp = token.getExpirationDate() + ApiToken.EXPIRATION_TIME;
        token.setExpirationDate(eStamp);
        return em.merge(token);
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
        if (token == null){
            throw new CardioDataException("can not find token with id = " + tokenId);
        }
        Long now = (new Date()).getTime();
        if ( now - token.getExpirationDate() >= ApiToken.EXPIRATION_TIME ){
            return true;
        }
        return false;
    }

    private String generateApiTokenString() {
        return StringUtils.generateRandomString(ApiToken.TOKEN_LENGTH);
    }
}
