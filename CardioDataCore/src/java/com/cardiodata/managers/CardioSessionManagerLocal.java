package com.cardiodata.managers;

import com.cardiodata.core.jpa.CardioSession;
import com.cardiodata.exceptions.CardioDataException;
import com.cardiodata.json.CardioSessionWithData;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author rogvold
 */
@Local
public interface CardioSessionManagerLocal {

    public CardioSession createCardioSession(Long userId, Long serverId, String dataClassName) throws CardioDataException;

    public CardioSession createCardioSession(CardioSession cs) throws CardioDataException;

    public CardioSession updateCardioSession(CardioSession cs) throws CardioDataException;
    
    public CardioSession updateCardioSession(Long sessionId, String newName, String newDescription) throws CardioDataException;
    
    public CardioSession renameCardioSession(Long sessionId, String newName) throws CardioDataException;
    
    public CardioSession getCardioSessionById(Long sessionId) throws CardioDataException;
    
    public void saveCardioSessionItems(Long sessionId, String jsonList) throws CardioDataException;
    
    public CardioSessionWithData getCardioSessionWihData(Long sessionId) throws CardioDataException;
    
    public void saveCardioSessionData(String jsonIncomingData) throws CardioDataException;
    
    public void rewriteCardioSessionData(String jsonIncomingData) throws CardioDataException;
    
    public List<CardioSession> getCardioSessionsOfUser(Long userId, Long serverId) throws CardioDataException;
    
    public boolean isSessionOfUser(Long userId, Long sessionId) throws CardioDataException;
    
    public void deleteCardioSession(Long sessionId) throws CardioDataException;
    
}
