/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cardiodata.managers;

import com.cardiodata.core.jpa.ClientServer;
import com.cardiodata.exceptions.CardioDataException;
import javax.ejb.Local;

/**
 *
 * @author rogvold
 */
@Local
public interface ClientServerManagerLocal {

    public ClientServer getClientServerById(Long cId) throws CardioDataException;
    
    public ClientServer createClientServer(String name) throws CardioDataException;
    
    public ClientServer updateClientServer(Long clientServerId, String newName) throws CardioDataException;
    
    
    
}
