package com.cardiodata.managers;

import com.cardiodata.core.jpa.User;
import com.cardiodata.core.jpa.UserGroup;
import com.cardiodata.core.jpa.UserGroupRequest;
import com.cardiodata.exceptions.CardioDataException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author rogvold
 */
@Local
public interface UserGroupManagerLocal {
    
    public void createUserGroup(Long ownerId, String name, String description) throws CardioDataException;
    
    public UserGroupRequest submitRequestToGroup(Long userId, Long invitorId, Long groupId) throws CardioDataException;
    
    public void acceptRequestToGroup(Long requestId) throws CardioDataException;
    
    public void rejectRequestToGroup(Long requestId) throws CardioDataException;
    
    public void removeUserFromGroup(Long groupId, Long userId) throws CardioDataException;
    
    public List<UserGroup> getUserGroups(Long userId) throws CardioDataException;

    public List<UserGroup> getGroupsOwnedByUser(Long ownerId) throws CardioDataException;
    
    public List<User> getUsersInGroup(Long groupId) throws CardioDataException;
    
    public List<UserGroupRequest> getRequestsToMe(Long userId) throws CardioDataException;
    
    public List<UserGroupRequest> getMyInvitationRequests(Long userId) throws CardioDataException;
    
    public void removeUserGroupRequest(Long userGroupRequestId) throws CardioDataException;
    
    public void inviteTrainee(Long trainerId, Long traineeId) throws CardioDataException;
    
    public void inviteToGroup(Long groupId, Long trainerId, String email) throws CardioDataException;
    
    public void removeTraineeInvitation(Long trainerId, Long traineeId) throws CardioDataException;
    
    public void removeUserGroup(Long groupId) throws CardioDataException;
    
    public List<User> getUserTrainers(Long userId) throws CardioDataException;
    
    public List<User> getInvitedTrainees(Long trainerId) throws CardioDataException;
    
}
