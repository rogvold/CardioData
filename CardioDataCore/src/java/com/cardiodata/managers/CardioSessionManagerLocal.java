package com.cardiodata.managers;

import com.cardiodata.core.jpa.CardioMoodSession;
import com.cardiodata.exceptions.CardioDataException;
import com.cardiodata.json.CardioSessionWithData;
import com.cardiodata.json.DashboardUser;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author rogvold
 */
@Local
public interface CardioSessionManagerLocal {

    public CardioMoodSession createCardioSession(Long userId, Long serverId, String dataClassName) throws CardioDataException;

    public CardioMoodSession createCardioSession(CardioMoodSession cs) throws CardioDataException;

    public CardioMoodSession updateCardioSession(CardioMoodSession cs) throws CardioDataException;
    
    public CardioMoodSession updateCardioSession(Long sessionId, String newName, String newDescription) throws CardioDataException;
    
    public CardioMoodSession renameCardioSession(Long sessionId, String newName) throws CardioDataException;
    
    public CardioMoodSession getCardioSessionById(Long sessionId) throws CardioDataException;
    
    public void saveCardioSessionItems(Long sessionId, String jsonList) throws CardioDataException;
    
    public CardioSessionWithData getCardioSessionWihData(Long sessionId) throws CardioDataException;
    
    public void saveCardioSessionData(String jsonIncomingData) throws CardioDataException;
    
    public void rewriteCardioSessionData(String jsonIncomingData) throws CardioDataException;
    
    public List<CardioMoodSession> getCardioSessionsOfUser(Long userId, Long serverId) throws CardioDataException;
    
    public boolean isSessionOfUser(Long userId, Long sessionId) throws CardioDataException;
    
    public void deleteCardioSession(Long sessionId) throws CardioDataException;
    
    public void finishCardioSession(Long sessionId, Long endTimestamp) throws CardioDataException;
    
    public Long getTheMostFreshCardioMoodSessionIdOfUser(Long userId) throws CardioDataException;
    
    public List<DashboardUser> getDashboardUsersOfTrainer(Long trainerId) throws CardioDataException;
    
}