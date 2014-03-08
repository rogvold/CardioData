package com.cardiodata.managers;

import com.cardiodata.core.jpa.CardioDataItem;
import com.cardiodata.core.jpa.CardioSession;
import com.cardiodata.exceptions.CardioDataException;
import com.cardiodata.json.CardioSessionWithData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author rogvold
 */
@Stateless
public class CardioSessionManager implements CardioSessionManagerLocal {

    @PersistenceContext(unitName = "CardioDataCorePU")
    EntityManager em;

    @Override
    public CardioSession createCardioSession(Long userId, Long serverId) throws CardioDataException {
        if (userId == null || serverId == null) {
            throw new CardioDataException("userId or serverId is null");
        }
        CardioSession cs = new CardioSession();
        cs.setUserId(userId);
        cs.setServerId(serverId);
        cs.setCreationTimestamp((new Date()).getTime());
        return em.merge(cs);
    }

    @Override
    public CardioSession createCardioSession(CardioSession cs) throws CardioDataException {
        CardioSession csess = createCardioSession(cs.getUserId(), cs.getServerId());
        csess.setDataClassName(cs.getDataClassName());
        csess.setDescription(cs.getDescription());
        csess.setName(cs.getName());
        return em.merge(csess);
    }

    @Override
    public CardioSession updateCardioSession(CardioSession cs) throws CardioDataException {
        if (cs.getId() == null) {
            throw new CardioDataException("cs id is null");
        }
        return em.merge(cs);
    }

    @Override
    public CardioSession updateCardioSession(Long sessionId, String newName, String newDescription) throws CardioDataException {
        if (sessionId == null) {
            throw new CardioDataException("sessionId is null");
        }
        CardioSession cs = getCardioSessionById(sessionId);
        if (newName == null || "".equals(newName)) {
            throw new CardioDataException("new name is empty");
        }
        cs.setName(newName);
        cs.setDescription(newDescription);
        return em.merge(cs);
    }

    @Override
    public CardioSession getCardioSessionById(Long sessionId) throws CardioDataException {
        CardioSession cs = em.find(CardioSession.class, sessionId);
        return cs;
    }

    @Override
    public void saveCardioSessionItems(Long sessionId, String jsonList) throws CardioDataException {
        CardioSession cs = getCardioSessionById(sessionId);
        if (cs == null) {
            throw new CardioDataException("session is not found");
        }
        List<CardioDataItem> list = new Gson().fromJson(jsonList, new TypeToken<List<CardioDataItem>>() {
        }.getType());
        for (CardioDataItem item : list) {
            CardioDataItem cdi = new CardioDataItem(item.getDataItem(), item.getSessionId(), item.getNumber(), item.getCreationTimestamp());
            em.merge(cdi);
        }
    }

    private List<CardioDataItem> getSessionCardioItems(Long sessionId) throws CardioDataException {
        CardioSession cs = getCardioSessionById(sessionId);
        if (cs == null) {
            throw new CardioDataException("cardiosession with id=" + sessionId + " is not found");
        }
        Query q = em.createQuery("select c from CardioDataItem c where c.sessionId=:sessionId order by c.number asc").setParameter("sessionId", sessionId);
        List<CardioDataItem> list = q.getResultList();
        if (list == null) {
            list = Collections.emptyList();
        }
        return list;
    }

    @Override
    public CardioSessionWithData getCardioSessionWihData(Long sessionId) throws CardioDataException {
        CardioSession cs = getCardioSessionById(sessionId);
        if (cs == null) {
            throw new CardioDataException("cardiosession with id=" + sessionId + " is not found");
        }
        List<CardioDataItem> list = getSessionCardioItems(sessionId);
        CardioSessionWithData cw = new CardioSessionWithData(list, sessionId, cs.getName(), cs.getDescription(), cs.getServerId(), cs.getUserId(), cs.getCreationTimestamp(), cs.getDataClassName());
        return cw;
    }

    @Override
    public void saveCardioSessionData(String jsonIncomingData) throws CardioDataException {
        CardioSessionWithData cw = (new Gson()).fromJson(jsonIncomingData, CardioSessionWithData.class);
        Long sessionId = cw.getId();
        if (sessionId == null) {
            CardioSession c = new CardioSession();
            c = em.merge(c);
            sessionId = c.getId();
        }
        cw.setId(sessionId);
        List<CardioDataItem> dataItems = cw.getDataItems();
        long n = 0;
        for (CardioDataItem cdi : dataItems) {
            CardioDataItem ci = new CardioDataItem(cdi.getDataItem(), sessionId, n, cdi.getCreationTimestamp());
            em.merge(ci);
        }
    }

    @Override
    public List<CardioSession> getCardioSessionsOfUser(Long userId, Long serverId) throws CardioDataException {
        Query q = em.createQuery("select c from CardioSession c where c.userId = :userId and c.serverId = :serverId").setParameter("userId", userId).setParameter("serverId", serverId);
        List<CardioSession> list = q.getResultList();
        if (list == null || list.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return list;
    }

    @Override
    public boolean isSessionOfUser(Long userId, Long sessionId) throws CardioDataException {
        Query q = em.createQuery("select c from CardioSession c where c.userId = :userId and c.id = :sessionId").setParameter("userId", userId).setParameter("sessionId", sessionId);
        List<CardioSession> list = q.getResultList();
        if (list == null || list.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public void deleteCardioSession(Long sessionId) throws CardioDataException {
        if (sessionId == null) {
            throw new CardioDataException("sessionId is null");
        }
        CardioSession c = getCardioSessionById(sessionId);
        if (c == null) {
            throw new CardioDataException("cardio session with id = " + sessionId + " is not found in the system");
        }
        em.remove(c);
    }
}
