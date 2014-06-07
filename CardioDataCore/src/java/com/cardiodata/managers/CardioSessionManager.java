package com.cardiodata.managers;

import com.cardiodata.core.jpa.CardioDataItem;
import com.cardiodata.core.jpa.CardioMoodSession;
import com.cardiodata.core.jpa.User;
import com.cardiodata.core.jpa.UserAccount;
import com.cardiodata.core.jpa.UserGroup;
import com.cardiodata.exceptions.CardioDataException;
import com.cardiodata.json.CalculatedRRSession;
import com.cardiodata.json.CardioSessionWithData;
import com.cardiodata.json.DashboardUser;
import com.cardiodata.json.ResponseConstants;
import com.cardiodata.json.entity.JsonRRInterval;
import com.cardiodata.utils.CalcManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.*;
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
public class CardioSessionManager implements CardioSessionManagerLocal {

    @PersistenceContext(unitName = "CardioDataCorePU")
    EntityManager em;
    
    @EJB
    UserGroupManagerLocal ugMan;
    
    @EJB
    UserManagerLocal userMan;
    

    @Override
    public CardioMoodSession createCardioSession(Long userId, Long serverId, String dataClassName) throws CardioDataException {
        if (userId == null || serverId == null) {
            throw new CardioDataException("userId or serverId is null");
        }
        CardioMoodSession cs = new CardioMoodSession();
        cs.setUserId(userId);
        cs.setServerId(serverId);
        cs.setCreationTimestamp((new Date()).getTime());
        cs.setDataClassName(dataClassName);
        return em.merge(cs);
    }

    @Override
    public CardioMoodSession createCardioSession(CardioMoodSession cs) throws CardioDataException {
        CardioMoodSession csess = createCardioSession(cs.getUserId(), cs.getServerId(), cs.getDataClassName());
        csess.setDataClassName(cs.getDataClassName());
        csess.setDescription(cs.getDescription());
        csess.setName(cs.getName());
        csess.setCreationTimestamp(cs.getCreationTimestamp());
        return em.merge(csess);
    }

    @Override
    public CardioMoodSession updateCardioSession(CardioMoodSession cs) throws CardioDataException {
        if (cs.getId() == null) {
            throw new CardioDataException("cs id is null");
        }
        return em.merge(cs);
    }

    @Override
    public CardioMoodSession updateCardioSession(Long sessionId, String newName, String newDescription) throws CardioDataException {
        if (sessionId == null) {
            throw new CardioDataException("sessionId is null");
        }
        CardioMoodSession cs = getCardioSessionById(sessionId);
        if (newName == null || "".equals(newName)) {
            throw new CardioDataException("new name is empty");
        }
        cs.setName(newName);
        cs.setDescription(newDescription);
        cs.setLastModificationTimestamp((new Date()).getTime());
        return em.merge(cs);
    }

    @Override
    public CardioMoodSession renameCardioSession(Long sessionId, String newName) throws CardioDataException {
        if (sessionId == null) {
            throw new CardioDataException("sessionId is null");
        }
        CardioMoodSession cs = getCardioSessionById(sessionId);
        if (newName == null || "".equals(newName)) {
            throw new CardioDataException("new name is empty");
        }
        cs.setName(newName);
        cs.setLastModificationTimestamp((new Date()).getTime());
        return em.merge(cs);
    }

    @Override
    public CardioMoodSession getCardioSessionById(Long sessionId) throws CardioDataException {
        CardioMoodSession cs = em.find(CardioMoodSession.class, sessionId);
        return cs;
    }

    @Override
    public void saveCardioSessionItems(Long sessionId, String jsonList) throws CardioDataException {
        CardioMoodSession cs = getCardioSessionById(sessionId);
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

    private List<CardioDataItem> getSessionCardioItems(Long sessionId, String className) throws CardioDataException {
        CardioMoodSession cs = getCardioSessionById(sessionId);
        if (cs == null) {
            throw new CardioDataException("cardiosession with id=" + sessionId + " is not found");
        }
        Query q = em.createQuery("select c from CardioDataItem c where c.sessionId=:sessionId order by c.number asc").setParameter("sessionId", sessionId);
        if ((className != null) && ("".equals(className) == false)){
            q = em.createQuery("select c from CardioDataItem c, CardioMoodSession cs where c.sessionId=:sessionId and c.sessionId=cs.id and  cs.dataClassName=:className order by c.number asc")
                    .setParameter("className", className)
                    .setParameter("sessionId", sessionId);
        }
        List<CardioDataItem> list = q.getResultList();
        if (list == null) {
            list = Collections.emptyList();
        }
        return list;
    }

    @Override
    public CardioSessionWithData getCardioSessionWihData(Long sessionId, String className) throws CardioDataException {
        CardioMoodSession cs = getCardioSessionById(sessionId);
        if (cs == null) {
            throw new CardioDataException("cardiosession with id=" + sessionId + " is not found");
        }
        List<CardioDataItem> list = getSessionCardioItems(sessionId, className);
        CardioSessionWithData cw = new CardioSessionWithData(list, sessionId, cs.getName(), cs.getDescription(), cs.getServerId(), cs.getUserId(), cs.getCreationTimestamp(), cs.getEndTimestamp(), cs.getDataClassName(), cs.getOriginalSessionId(), cs.getLastModificationTimestamp());
        return cw;
    }

    @Override
    public void saveCardioSessionData(String jsonIncomingData) throws CardioDataException {
        //System.out.println("saveCardioSessionData occured");
        //System.out.println("jsonIncomingData = " + jsonIncomingData);
        CardioSessionWithData cw = (new Gson()).fromJson(jsonIncomingData, CardioSessionWithData.class);
        Long sessionId = cw.getId();
        if (sessionId == null) {
            CardioMoodSession c = new CardioMoodSession();
            c = em.merge(c);
            sessionId = c.getId();
        }
        cw.setId(sessionId);
        List<CardioDataItem> dataItems = cw.getDataItems();
        CardioMoodSession session = getCardioSessionById(sessionId);

//        Long extModifDate = Math.min(cw.getLastModificationTimestamp(), (new Date()).getTime());
//        if (extModifDate < session.getLastModificationTimestamp()) {
//            throw new CardioDataException("session was updated on server", ResponseConstants.SESSION_IS_MODIFIED_ON_SERVER_CODE);
//        }
//        session.setDataClassName(cw.getDataClassName());
        em.merge(session);
        List<CardioDataItem> oldList = getSessionCardioItems(sessionId, null);
        dataItems = getNewDataItems(oldList, dataItems);
        for (CardioDataItem cdi : dataItems) {
            CardioDataItem ci = new CardioDataItem(cdi.getDataItem(), sessionId, cdi.getNumber(), cdi.getCreationTimestamp());
            em.merge(ci);
        }
    }

    @Override
    public void rewriteCardioSessionData(String jsonIncomingData) throws CardioDataException {
        CardioSessionWithData cw = (new Gson()).fromJson(jsonIncomingData, CardioSessionWithData.class);
        Long sessionId = cw.getId();
        System.out.println("rewriteCardioSessionData: sessionId = " + sessionId + "");
        deleteCardioDataItems(sessionId);
        List<CardioDataItem> dataItems = cw.getDataItems();
        CardioMoodSession session = getCardioSessionById(sessionId);

        if (session == null) {
            throw new CardioDataException("can not find session with sessionId=" + sessionId + "", ResponseConstants.NORMAL_ERROR_CODE);
        }

        if (cw.getLastModificationTimestamp() == null) {
            cw.setLastModificationTimestamp((new Date()).getTime());
        }

        if (session.getLastModificationTimestamp() == null) {
            session.setLastModificationTimestamp((new Date()).getTime());
        }

        Long extModifDate = Math.min(cw.getLastModificationTimestamp(), (new Date()).getTime());
        if (extModifDate < session.getLastModificationTimestamp()) {
            throw new CardioDataException("session was updated on server", ResponseConstants.SESSION_IS_MODIFIED_ON_SERVER_CODE);
        }

        session.setName(cw.getName());
        session.setDescription(cw.getDescription());
        session.setDataClassName(cw.getDataClassName());
        session.setCreationTimestamp(cw.getCreationTimestamp());
        session.setLastModificationTimestamp(cw.getLastModificationTimestamp() == null ? (new Date()).getTime() : cw.getLastModificationTimestamp());
        session.setOriginalSessionId(cw.getOriginalSessionId());
        session.setEndTimestamp(cw.getEndTimestamp());
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
        //System.out.println("set = ");
        //System.out.println(stamps);

        for (CardioDataItem d : newList) {
            if (stamps.contains(d.getNumber())) {
                //System.out.println("skipping number " + d.getNumber());
                continue;
            } else {
                stamps.add(d.getNumber());
                l.add(d);
                //System.out.println("adding number " + d.getNumber());
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
    public List<CardioMoodSession> getCardioSessionsOfUser(Long userId, Long serverId) throws CardioDataException {
        Query q = em.createQuery("select c from CardioMoodSession c where c.userId = :userId and c.serverId = :serverId").setParameter("userId", userId).setParameter("serverId", serverId);
        List<CardioMoodSession> list = q.getResultList();
        if (list == null || list.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return list;
    }

    @Override
    public boolean isSessionOfUser(Long userId, Long sessionId) throws CardioDataException {
        Query q = em.createQuery("select c from CardioMoodSession c where c.userId = :userId and c.id = :sessionId").setParameter("userId", userId).setParameter("sessionId", sessionId);
        List<CardioMoodSession> list = q.getResultList();
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
        CardioMoodSession c = getCardioSessionById(sessionId);
        if (c == null) {
            throw new CardioDataException("cardio session with id = " + sessionId + " is not found in the system");
        }
        em.remove(c);
    }

    @Override
    public void finishCardioSession(Long sessionId, Long endTimestamp) throws CardioDataException {
        if (sessionId == null){
            throw new CardioDataException("sessionId is null");
        }
        if (endTimestamp == null){
            throw new CardioDataException("endTimestamp is null");
        }
        CardioMoodSession cs = getCardioSessionById(sessionId);
        if (cs == null){
            throw new CardioDataException("session with id=" + sessionId + " is not found"  );
        }
        if (cs.getCreationTimestamp() > endTimestamp){
            throw new CardioDataException("finishCardioSession: endTimestamp is more than creationTimestamp");
        }
        cs.setEndTimestamp(endTimestamp);
        em.merge(cs);
    }

    @Override
    public Long getTheMostFreshCardioMoodSessionIdOfUser(Long userId) throws CardioDataException {
        if (userId == null){
            throw new CardioDataException("userId is null");
        }
        List<Long> fList = em.createQuery("select max(item.creationTimestamp) from CardioDataItem item, CardioMoodSession cs where cs.userId=:userId and item.sessionId=cs.id")
                .setParameter("userId", userId).setMaxResults(0).getResultList();
        if (fList == null || fList.isEmpty()){
            return null;
        }
        Long tStamp = fList.get(0);
        List<Long> list = em.createQuery("select item.sessionId from CardioDataItem item where item.creationTimestamp=:stamp")
                .setParameter("stamp", tStamp)
                .getResultList();
        if (list == null || list.isEmpty()){
            return null;
        }
        Long sessionId = list.get(0);
        System.out.println("getTheMostFreshCardioMoodSessionIdOfUser: the most fresh session of user (id=" + userId +") is session with id=" + sessionId);
        return sessionId;
    }

    private DashboardUser getDashboardUser(User u) throws CardioDataException{
        if (u == null){
            throw new CardioDataException("User is null");
        }
        
        Long sessionId = getTheMostFreshCardioMoodSessionIdOfUser(u.getId());
        CardioSessionWithData d = getCardioSessionWihData(sessionId, JsonRRInterval.class.getSimpleName());
        List<CardioDataItem> items = d.getDataItems();
        double[] arr = CalcManager.getArrayFromRRCardioDataItemList(items);
        
        Double bpm = null;
        Double lastSDNN = null;
        if (!items.isEmpty()){
            bpm = CalcManager.getLastFilteredBPM(arr);
            lastSDNN = CalcManager.getLastSDNN(arr);
        }
        int from = Math.max(0, arr.length - 50);
        int to = arr.length;
        List<Double> lastList = new ArrayList();
        double[] arr2 = Arrays.copyOfRange(arr, from, to);
        arr2 = CalcManager.pisarukFilter(arr2);
        for (int i = 0; i < arr2.length; i++){
            lastList.add(arr2[i]);
        }
        DashboardUser du = new DashboardUser();
        du.setBpm(bpm);
        du.setSDNN(lastSDNN);
        du.setId(u.getId());
        du.setLastIntervals(lastList);
        du.setLastUpdatedTimestamp(items.get(items.size() - 1).getCreationTimestamp());
        String firstName = u.getFirstName();
        if ((firstName == null || "".equals(firstName)) && (u.getLastName() == null || "".equals(u.getLastName())) ){
            UserAccount ac = userMan.getUserAccountByUserId(u.getId());
            firstName = ac.getLogin();
        }
        du.setFirstName(firstName);
        du.setLastName(u.getLastName());
        
        return du;
    }
    
    @Override
    public List<DashboardUser> getDashboardUsersOfTrainer(Long trainerId) throws CardioDataException {
        if (trainerId == null){
            throw new CardioDataException("trainerId is null");
        }
        UserGroup g = userMan.getTrainerDefaultGroup(trainerId);
        List<User> users = ugMan.getUsersInGroup(g.getId());
        List<DashboardUser> list = new ArrayList();
        for (User u : users){
            list.add(getDashboardUser(u));
        }
        return list;
    }

    @Override
    public CalculatedRRSession getCalculatedRRSession(Long sessionId, boolean useFilter) throws CardioDataException {
        if (sessionId == null){
            throw new CardioDataException("getCalculatedSession: sessionId is null");
        }
        CardioMoodSession cs = getCardioSessionById(sessionId);
        if (cs == null){
            throw new CardioDataException("session with id=" + sessionId + " is not found");
        }
        CardioSessionWithData d = getCardioSessionWihData(sessionId, JsonRRInterval.class.getSimpleName());
        List<CardioDataItem> items = d.getDataItems();
        double[][] arr = CalcManager.get2DArrayFromRRCardioDataItemList(items);
        if (useFilter == true){
            arr = CalcManager.filter2DRRArray(arr);
        }
        System.out.println("getCalculatedRRSession: arr = ");
        
        System.out.println(arr);
        System.out.println("arr.length = " + arr[0].length);
        
        Map<String, double[][]> map = new HashMap();
        map.put("RR", arr);
        
        double[][] arrSDNN = CalcManager.getSDNN(arr[1], arr[0], false);
        map.put("SDNN", arrSDNN);
        
        //waitung for Anton bug fix( HeartRateUtils - 69 and 184)
        //double[][] arrTension = CalcManager.getTensionArray(arr[1], arr[0], false);
        //map.put("SI", arrTension);
        
        return new CalculatedRRSession(cs, map);
    }

    @Override
    public List<CardioDataItem> getCardioDataItemsBetweenTwoDates(Long userId, Long fromTimestamp, Long toTimestamp, String className) throws CardioDataException {
        if (userId == null){
            throw new CardioDataException("userId is null");
        }
        if (fromTimestamp == null){
            throw new CardioDataException("fromTimestamp is null");
        }
        if (toTimestamp == null){
            throw new CardioDataException("toTimestamp is null");
        }
        if (className == null || "".equals(className)){
            throw new CardioDataException("className is not specified");
        }
        List<CardioDataItem> list = em.createQuery("select item from CardioDataItem item, CardioMoodSession cs where "
                + " cs.userId=:userId and item.sessionId=cs.id and cs.dataClassName=:className and "
                + " item.creationTimestamp > :fromTimestamp and "
                + " item.creationTimestamp < :toTimestamp order by item.creationTimestamp asc")
                .setParameter("userId", userId)
                .setParameter("fromTimestamp", fromTimestamp)
                .setParameter("toTimestamp", toTimestamp)
                .setParameter("className", className)
                .getResultList();
        if (list == null || list.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        return list;
    }
    
    
}