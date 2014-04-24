package com.cardiodata.managers;

import com.cardiodata.exceptions.CardioDataException;
import javax.ejb.Local;

/**
 *
 * @author rogvold
 */
@Local
public interface UserGroupManagerLocal {
    
    public void createUserGroup(Long ownerId, String name, String description) throws CardioDataException;
    
    
    
}
