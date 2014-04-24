package com.cardiodata.managers;

import com.cardiodata.core.jpa.CardioDataItem;
import com.cardiodata.core.jpa.CardioSession;
import com.cardiodata.exceptions.CardioDataException;
import com.cardiodata.json.CardioSessionWithData;
import com.cardiodata.json.ResponseConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.*;
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
    public CardioSession createCardioSession(Long userId, Long serverId, String dataClassName) throws CardioDataException {
        if (userId == null || serverId == null) {
            throw new CardioDataException("userId or serverId is null");
        }
        CardioSession cs = new CardioSession();
        cs.setUserId(userId);
        cs.setServerId(serverId);
        cs.setCreationTimestamp((new Date()).getTime());
        cs.setDataClassName(dataClassName);
        return em.merge(cs);
    }

    @Override
    public CardioSession createCardioSession(CardioSession cs) throws CardioDataException {
        CardioSession csess = createCardioSession(cs.getUserId(), cs.getServerId(), cs.getDataClassName());
        csess.setDataClassName(cs.getDataClassName());
        csess.setDescription(cs.getDescription());
        csess.setName(cs.getName());
        csess.setCreationTimestamp(cs.getCreationTimestamp());
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
        cs.setLastModificationTimestamp((new Date()).getTime());
        return em.merge(cs);
    }

    @Override
    public CardioSession renameCardioSession(Long sessionId, String newName) throws CardioDataException {
        if (sessionId == null) {
            throw new CardioDataException("sessionId is null");
        }
        CardioSession cs = getCardioSessionById(sessionId);
        if (newName == null || "".equals(newName)) {
            throw new CardioDataException("new name is empty");
        }
        cs.setName(newName);
        cs.setLastModificationTimestamp((new Date()).getTime());
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
        CardioSessionWithData cw = new CardioSessionWithData(list, sessionId, cs.getName(), cs.getDescription(), cs.getServerId(), cs.getUserId(), cs.getCreationTimestamp(), cs.getDataClassName(), cs.getOriginalSessionId(), cs.getLastModificationTimestamp());
        return cw;
    }

    @Override
    public void saveCardioSessionData(String jsonIncomingData) throws CardioDataException {
        System.out.println("saveCardioSessionData occured");
        System.out.println("jsonIncomingData = " + jsonIncomingData);
        CardioSessionWithData cw = (new Gson()).fromJson(jsonIncomingData, CardioSessionWithData.class);
        Long sessionId = cw.getId();
        if (sessionId == null) {
            CardioSession c = new CardioSession();
            c = em.merge(c);
            sessionId = c.getId();
        }
        cw.setId(sessionId);
        List<CardioDataItem> dataItems = cw.getDataItems();
        CardioSession session = getCardioSessionById(sessionId);

//        Long extModifDate = Math.min(cw.getLastModificationTimestamp(), (new Date()).getTime());
//        if (extModifDate < session.getLastModificationTimestamp()) {
//            throw new CardioDataException("session was updated on server", ResponseConstants.SESSION_IS_MODIFIED_ON_SERVER_CODE);
//        }
//        session.setDataClassName(cw.getDataClassName());
        em.merge(session);
        List<CardioDataItem> oldList = getSessionCardioItems(sessionId);
        dataItems = getNewDataItems(oldList, dataItems);
        for (CardioDataItem cdi : dataItems) {
            CardioDataItem ci = new CardioDataItem(cdi.getDataItem(), sessionId, cdi.getNumber(), cdi.getCreationTimestamp());
            em.merge(ci);
        }
    }

    @Override
    public void rewriteCardioSessionData(String jsonIncomingData) throws CardioDataException {//todo
        CardioSessionWithData cw = (new Gson()).fromJson(jsonIncomingData, CardioSessionWithData.class);
        Long sessionId = cw.getId();
        deleteCardioDataItems(sessionId);
        List<CardioDataItem> dataItems = cw.getDataItems();
        CardioSession session = getCardioSessionById(sessionId);

        if (cw.getLastModificationTimestamp() == null) {
            cw.setLastModificationTimestamp((new Date()).getTime());
        }
        Long extModifDate = Math.min(cw.getLastModificationTimestamp(), (new Date()).getTime());
        if (extModifDate < session.getLastModificationTimestamp()) {
            throw new CardioDataException("session was updated on server", ResponseConstants.SESSION_IS_MODIFIED_ON_SERVER_CODE);
        }

        session.setName(cw.getName());
        session.setDescription(cw.getDescription());
        session.setCreationTimestamp(cw.getCreationTimestamp());
        session.setLastModificationTimestamp(cw.getLastModificationTimestamp() == null ? (new Date()).getTime() : cw.getLastModificationTimestamp());
        session.setOriginalSessionId(cw.getOriginalSessionId());
        em.merge(session);
        for (CardioDataItem cdi : dataItems) {
            CardioDataItem ci = new CardioDataItem(cdi.getDataItem(), sessionId, cdi.getNumber(), cdi.getCreationTimestamp());
            em.merge(ci);
        }
    }

    private void deleteCardioDataItems(Long cardioSessionId) throws CardioDataException {
        if (cardioSessionId == null) {
            throw new CardioDataException("cardioSessionId is null");
        }
        Query q = em.createQuery("delete from CardioDataItem item where item.sessionId = :sId").setParameter("sId", cardioSessionId);
        q.executeUpdate();
    }

    private List<CardioDataItem> getNewDataItems(List<CardioDataItem> oldList, List<CardioDataItem> newList) {
        if (oldList == null) {
            oldList = Collections.emptyList();
        }
        List<CardioDataItem> l = new ArrayList();
        Set<Long> stamps = new HashSet();

        for (CardioDataItem d : oldList) {
            stamps.add(d.getNumber());
        }
        System.out.println("set = ");
        System.out.println(stamps);

        for (CardioDataItem d : newList) {
            if (stamps.contains(d.getNumber())) {
                System.out.println("skipping number " + d.getNumber());
                continue;
            } else {
                stamps.add(d.getNumber());
                l.add(d);
                System.out.println("adding number " + d.getNumber());
            }
        }
        if (l.size() <= 1) {
            return l;
        }
//        Collections.sort(l, new Comparator<CardioDataItem>() {
//            @Override
//            public int compare(CardioDataItem s1, CardioDataItem s2) {
//                return s1.getNumber().compareTo(s2.getNumber());
//            }
//        });
//        List<CardioDataItem> res = new ArrayList();
//        res.add(l.get(0));
//        for (int i = 1; i < l.size(); i++){
//            CardioDataItem item = l.get(i);
//            item.setCreationTimestamp(l.get(i - 1).getCreationTimestamp() + item.get);
//        }
        return l;
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
