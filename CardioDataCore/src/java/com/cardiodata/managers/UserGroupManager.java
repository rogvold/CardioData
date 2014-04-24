package com.cardiodata.managers;

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
    
    
    
    
}
