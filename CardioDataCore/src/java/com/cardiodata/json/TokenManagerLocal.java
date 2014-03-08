package com.cardiodata.json;

import com.cardiodata.core.jpa.ApiToken;
import com.cardiodata.exceptions.CardioDataException;
import javax.ejb.Local;

/**
 *
 * @author rogvold
 */
@Local
public interface TokenManagerLocal {

//    public ApiToken refreshUserToken(Long userId) throws CardioDataException;
    public void assertToken(Long userId, String tokenString) throws CardioDataException;
    
    public ApiToken getCurrentToken(Long userId) throws CardioDataException;
    
    public ApiToken getTokenById(Long tokenId) throws CardioDataException;
    
    public ApiToken getTokenByUserId(Long userId) throws CardioDataException;
    
    public boolean isExpiredApiToken(Long tokenId) throws CardioDataException;
    
}
