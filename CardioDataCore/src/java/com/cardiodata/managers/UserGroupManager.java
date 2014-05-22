package com.cardiodata.managers;

import com.cardiodata.core.jpa.UserGroup;
import com.cardiodata.core.jpa.UserGroupBinding;
import com.cardiodata.core.jpa.UserGroupRequest;
import com.cardiodata.enums.UserGroupPrivecyEnum;
import com.cardiodata.enums.UserGroupRequestStatusEnum;
import com.cardiodata.enums.UserGroupStatusEnum;
import com.cardiodata.enums.UserGroupTypeEnum;
import com.cardiodata.exceptions.CardioDataException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author rogvold
 */
@Stateless
public class UserGroupManager implements UserGroupManagerLocal {

    @PersistenceContext(unitName = "CardioDataCorePU")
    EntityManager em;

    private UserGroup getUserGroupByName(String name, Long ownerId) {
        List<UserGroup> list = em.createQuery("select g from UserGroup g where q.name = :name and g.ownerId = :ownerId").setParameter("name", name).setParameter("ownerId", ownerId).getResultList();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    private UserGroupRequest getRequest(Long userId, Long invitorId, Long groupId) throws CardioDataException {
        if (userId == null) {
            throw new CardioDataException("getRequest: userId is null");
        }
        if (invitorId == null) {
            throw new CardioDataException("getRequest: invitorId is null");
        }
        if (groupId == null) {
            throw new CardioDataException("getRequest: groupId is null");
        }
        List<UserGroupRequest> list = em.createQuery("select r from UserGroupRequest r where r.userId=:userId and r.invitorId=:invitorId and r.groupId = :groupId").setParameter("userId", userId).setParameter("invitorId", invitorId).setParameter("groupId", groupId).getResultList();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public void createUserGroup(Long ownerId, String name, String description) throws CardioDataException {
        UserGroup ugr = getUserGroupByName(name, ownerId);
        if (ugr != null) {
            throw new CardioDataException("Group with ownerId=" + ownerId + " and name = " + name + " already exists in the system");
        }
        UserGroup ug = new UserGroup(name, description, (new Date()).getTime(), ownerId, UserGroupTypeEnum.GENERAL, UserGroupStatusEnum.ACTIVE, UserGroupPrivecyEnum.PUBLIC);
        em.merge(ug);
    }

    @Override
    public UserGroupRequest submitRequestToGroup(Long userId, Long invitorId, Long groupId) throws CardioDataException {
        UserGroupRequest req = getRequest(userId, invitorId, groupId);
        if (req != null) {
            throw new CardioDataException("submitRequestToGroup: request has already been submitted (userId=" + userId + "; invitorId=" + invitorId + "; groupId=" + groupId + ")");
        }
        req = new UserGroupRequest(userId, invitorId, groupId, UserGroupRequestStatusEnum.PENDING);
        return em.merge(req);
    }

    @Override
    public void acceptRequestToGroup(Long requestId) throws CardioDataException {
        if (requestId == null) {
            throw new CardioDataException("acceptRequestToGroup: requestId is null");
        }
        UserGroupRequest req = em.find(UserGroupRequest.class, requestId);
        if (req == null) {
            throw new CardioDataException("request with requestId = " + requestId + " is not found");
        }
        Long groupId = req.getGroupId();
        Long userId = req.getUserId();
        UserGroupBinding b = new UserGroupBinding(userId, groupId);
        em.merge(b);
        em.remove(req);
    }

    @Override
    public void rejectRequestToGroup(Long requestId) throws CardioDataException {
        if (requestId == null) {
            throw new CardioDataException("acceptRequestToGroup: requestId is null");
        }
        UserGroupRequest req = em.find(UserGroupRequest.class, requestId);
        if (req == null) {
            throw new CardioDataException("request with requestId = " + requestId + " is not found");
        }
        em.remove(req);
    }

    @Override
    public List<UserGroup> getUserGroups(Long userId) throws CardioDataException {
        if (userId == null) {
            throw new CardioDataException("getUserGroups: userId is null");
        }
        List<UserGroup> list = em.createQuery("select g from UserGroup g, UserGroupBinding b where g.id=b.groupId and b.userId=:userId").setParameter("userId", userId).getResultList();
        if (list == null || list.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return list;
    }

    @Override
    public List<UserGroupRequest> getMyRequests(Long userId) throws CardioDataException {
        if (userId == null) {
            throw new CardioDataException("getMyRequests: userId is null");
        }
        List<UserGroupRequest> list = em.createQuery("select r from UserGroupRequest r where r.userId=:userId").setParameter("userId", userId).getResultList();
        if (list == null || list.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return list;
    }

    @Override
    public List<UserGroupRequest> getMyInvitationRequests(Long userId) throws CardioDataException {
        if (userId == null) {
            throw new CardioDataException("getMyRequests: userId is null");
        }
        List<UserGroupRequest> list = em.createQuery("select r from UserGroupRequest r where r.invitorId=:userId").setParameter("userId", userId).getResultList();
        if (list == null || list.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return list;
    }

    @Override
    public void removeUserFromGroup(Long groupId, Long userId) throws CardioDataException {
        if (groupId == null) {
            throw new CardioDataException("removeUserFromGroup: groupId is null");
        }
        if (userId == null) {
            throw new CardioDataException("removeUserFromGroup: userId is null");
        }
        List<UserGroupBinding> list = em.createQuery("select b from UserGroupBinding b where b.groupId=:groupId and b.userId = :userId").setParameter("groupId", groupId).setParameter("userId", userId).getResultList();
        if (list == null || list.isEmpty()) {
            return;
        }
        UserGroupBinding b = list.get(0);
        em.remove(b);
    }
}
