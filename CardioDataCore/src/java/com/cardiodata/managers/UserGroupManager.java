package com.cardiodata.managers;

import com.cardiodata.core.jpa.User;
import com.cardiodata.core.jpa.UserGroup;
import com.cardiodata.core.jpa.UserGroupBinding;
import com.cardiodata.core.jpa.UserGroupRequest;
import com.cardiodata.enums.UserGroupPrivacyEnum;
import com.cardiodata.enums.UserGroupRequestStatusEnum;
import com.cardiodata.enums.UserGroupStatusEnum;
import com.cardiodata.enums.UserGroupTypeEnum;
import com.cardiodata.enums.UserRoleEnum;
import com.cardiodata.exceptions.CardioDataException;
import com.cardiodata.json.ResponseConstants;
import com.cardiodata.utils.StringUtils;
import java.util.Collections;
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
public class UserGroupManager implements UserGroupManagerLocal {
    


    @PersistenceContext(unitName = "CardioDataCorePU")
    EntityManager em;
    
    @EJB
    UserManagerLocal userMan;

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
        UserGroup ug = new UserGroup(name, description, (new Date()).getTime(), ownerId, UserGroupTypeEnum.GENERAL, UserGroupStatusEnum.ACTIVE, UserGroupPrivacyEnum.PUBLIC);
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
        req.setRequestStatus(UserGroupRequestStatusEnum.ACCEPTED);
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
    public List<UserGroup> getGroupsOwnedByUser(Long ownerId) throws CardioDataException {
        if (ownerId == null) {
            throw new CardioDataException("getGroupsOwnedByUser: ownerId is null");
        }
        List<UserGroup> list = em.createQuery("select g from UserGroup g, UserGroupBinding b where g.id=b.groupId and g.ownerId=:ownerId").setParameter("ownerId", ownerId).getResultList();
        if (list == null || list.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return list;
    }
    
    
    @Override
    public List<User> getUsersInGroup(Long groupId) throws CardioDataException {
        if (groupId == null){
            throw new CardioDataException("getUsersInGroup: groupId is null");
        }
        List<User> list = em.createQuery("select u from User u, UserGroupBinding b where b.groupId=:groupId and b.userId=u.id and u.userRole=:uRole")
                .setParameter("uRole", UserRoleEnum.USER)
                .setParameter("groupId", groupId).getResultList();
        if (list == null || list.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        return list;
    }
    
    



    @Override
    public List<UserGroupRequest> getRequestsToMe(Long userId) throws CardioDataException {
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
    
    
    @Override
    public void removeUserGroupRequest(Long userGroupRequestId) throws CardioDataException {
        if (userGroupRequestId == null){
            throw new CardioDataException("userGroupRequestId is null");
        }
        UserGroupRequest req = em.find(UserGroupRequest.class, userGroupRequestId);
        if (req == null){
            throw new CardioDataException("cannot find UserGroupRequest with id=" + userGroupRequestId);
        }
        if (UserGroupRequestStatusEnum.PENDING.equals(req.getRequestStatus())){
            throw new CardioDataException("cannot delete pending request");
        }
        em.remove(req);
    }

    @Override
    public void inviteTrainee(Long trainerId, Long traineeId) throws CardioDataException {
        if (trainerId == null){
            throw new CardioDataException("inviteTrainee: trainerId is null");
        }
        if (traineeId == null){
            throw new CardioDataException("inviteTrainee: traineeId is null");
        }
        User trainer = userMan.getUserById(trainerId);
        if (trainer == null){
            throw new CardioDataException("trainer is not found");
        }
        if (UserRoleEnum.TRAINER.equals(trainer.getUserRole()) == false){
            throw new CardioDataException("user with id " + trainerId + " is not a trainer");
        }
        User trainee = userMan.getUserById(traineeId);
        if (trainee == null){
            throw new CardioDataException("trainee is not found");
        }
        if (UserRoleEnum.USER.equals(trainee.getUserRole()) == false){
            throw new CardioDataException("user with id = " + traineeId + " is not a trainee");
        }
        
        UserGroup g = userMan.getTrainerDefaultGroup(trainerId);
        if (g == null){
            throw new CardioDataException("trainer has no default group");
        }
        submitRequestToGroup(traineeId, trainerId, g.getId());
    }
    
    
    @Override
    public void inviteToGroup(Long groupId, Long trainerId, String email) throws CardioDataException {
        if (groupId == null){
            throw new CardioDataException("inviteToGroup: groupId is null");
        }
        if (email == null || "".equals(email)){
            throw new CardioDataException("inviteToGroup: email is empty");
        }
        if (trainerId == null){
            throw new CardioDataException("inviteToGroup: trainerId is null");
        }
        if (StringUtils.isValidEmail(email) == false){
            throw new CardioDataException("inviteToGroup: email '" + email + "' is not valid");
        };
        UserGroup g = em.find(UserGroup.class, groupId);
        if (g == null){
            throw new CardioDataException("inviteToGroup: group with id=" + groupId + " is not found in the system");
        }
        User u = userMan.getUserByEmail(email);
        if (u == null){
            throw new CardioDataException("User with email '" + email + "' is not found");
        }
        Long userId = u.getId();
        Long ownerId = g.getOwnerId();
        if (ownerId.equals(trainerId) == false){
            throw new CardioDataException("trainer is not the owner of the group", ResponseConstants.ACCESS_DENIED_CODE);
        }
        submitRequestToGroup(userId, ownerId, groupId);
    }

    
    @Override
    public void removeTraineeInvitation(Long trainerId, Long traineeId) throws CardioDataException {
        System.out.println("removeTraineeInvitation: trainerId=" + trainerId + "; traineeId = " + traineeId);
        if (trainerId == null){
            throw new CardioDataException("removeTraineeInvitation: trainerId is null");
        }
        if (traineeId == null){
            throw new CardioDataException("removeTraineeInvitation: traineeId is null");
        }
        UserGroup g = userMan.getTrainerDefaultGroup(trainerId);
        if (g == null){
            throw new CardioDataException("cannot find trainer default group");
        }
        UserGroupRequest req = getRequest(traineeId, trainerId, g.getId());
        if (UserGroupRequestStatusEnum.PENDING.equals(req.getRequestStatus()) == false){
            throw new CardioDataException("cannot remove UserGroupRequest. status = " + req.getRequestStatus());
        }
        em.remove(req);
    }

    @Override
    public void removeUserGroup(Long groupId) throws CardioDataException {
        if (groupId == null){
            throw new CardioDataException();
        }
        List<User> list = getUsersInGroup(groupId);
        if (list.isEmpty() == false){
            throw new CardioDataException("cannot remove group - it is not empty");
        }
        Query q = em.createQuery("delete from UserGroupRequest r where r.groupId=:groupId").setParameter("groupId", groupId);
        q.executeUpdate();
    }

    @Override
    public List<User> getUserTrainers(Long userId) throws CardioDataException {
        if (userId == null){
            throw new CardioDataException("getUserTrainers: userIs is not specified");
        }
        List<User> list = em.createQuery("select t from User t, User u, UserGroupBinding ub, UserGroup ug where ug.id=ub.groupId and ub.userId=u.id and u.id=:userId and ug.groupType=:gType and ug.ownerId=t.id")
                .setParameter("userId", userId)
                .setParameter("gType", UserGroupTypeEnum.DEFAULT)
                .getResultList();
        if (list == null || list.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        return list;
    }

    @Override
    public List<User> getInvitedTrainees(Long trainerId) throws CardioDataException {
        if (trainerId == null){
            throw new CardioDataException("trainerId is not specified");
        }
        UserGroup g = userMan.getTrainerDefaultGroup(trainerId);
        if (g == null){
            throw new CardioDataException("default group is not found");
        }
        List<User> list = em.createQuery("select u from User u, UserGroupRequest r where r.userId=u.id and r.groupId=:groupId")
                .setParameter("groupId", g.getId())
                .getResultList();
        if (list == null || list.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        return list;
    }

    @Override
    public void inviteTrainee(Long trainerId, String traineeEmail) throws CardioDataException {
        if (trainerId == null){
            throw new CardioDataException("traineeId is not defined");
        }
        if (traineeEmail == null || "".equals(traineeEmail)){
            throw new CardioDataException("traineeEmail is not specified");
        }
        User trainee = userMan.getUserByEmail(traineeEmail);
        if (trainee == null){
            throw new CardioDataException("Trainee with specified email is not found in the system.");
        }
        inviteTrainee(trainerId, trainee.getId());
    }

    @Override
    public List<User> getMyInvitorsTrainers(Long userId) throws CardioDataException {
        if (userId == null){
            throw new CardioDataException("userId is not specified");
        }
        User u = userMan.getUserById(userId);
        if (u == null){
            throw new CardioDataException("user with id=" + userId + " is not found in the system");
        }
        if (UserRoleEnum.USER.equals(u.getUserRole()) == false){
            throw new CardioDataException("user with id=" + userId +" is not a USER - his role is '" + u.getUserRole() + "'");
        }
        List<User> list = em.createQuery("select t from User t, UserGroupRequest r where r.userId=:userId and r.invitorId=t.id")
                .setParameter("userId", userId)
                .getResultList();
        if (list == null || list.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        return list;
    }

    @Override
    public void rejectToTrainer(Long userId, Long trainerId) throws CardioDataException {
        if (trainerId == null){
            throw new CardioDataException("trainerId is null");
        }
        UserGroup g = userMan.getTrainerDefaultGroup(trainerId);
        UserGroupRequest r = getRequest(userId, trainerId, g.getId());
        if (r == null){
            return;
        }
        em.remove(r);
    }

    @Override
    public void acceptToTrainer(Long userId, Long trainerId) throws CardioDataException {
        if (trainerId == null){
            throw new CardioDataException("trainerId is null");
        }
        UserGroup g = userMan.getTrainerDefaultGroup(trainerId);
        UserGroupRequest r = getRequest(userId, trainerId, g.getId());
        acceptRequestToGroup(r.getId());
    }

    
}
