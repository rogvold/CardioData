package com.cardiodata.managers;

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
    
    public List<UserGroupRequest> getMyRequests(Long userId) throws CardioDataException;
    
    public List<UserGroupRequest> getMyInvitationRequests(Long userId) throws CardioDataException;
}
