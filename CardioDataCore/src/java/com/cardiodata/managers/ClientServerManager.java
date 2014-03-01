package com.cardiodata.managers;

import com.cardiodata.core.jpa.ClientServer;
import com.cardiodata.exceptions.CardioDataException;
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
public class ClientServerManager implements ClientServerManagerLocal {

    @PersistenceContext(unitName = "CardioDataCorePU")
    EntityManager em;

    @Override
    public ClientServer getClientServerById(Long cId) throws CardioDataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private ClientServer getClientServerByName(String name) {
        Query q = em.createQuery("select c from ClientServer c where c.name=:name").setParameter("name", name);
        List<ClientServer> list = q.getResultList();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public ClientServer createClientServer(String name) throws CardioDataException {
        ClientServer c = getClientServerByName(name);
        if (c != null) {
            throw new CardioDataException("ClientServer with name = " + name + " already registered in the system");
        }
        c = new ClientServer(name, (new Date()).getTime());
        return em.merge(c);
    }

    @Override
    public ClientServer updateClientServer(Long clientServerId, String newName) throws CardioDataException {
        ClientServer c = getClientServerById(clientServerId);
        if (c == null){
            throw new CardioDataException("ClientServer with id = " + clientServerId + " does not exist in the system");
        }
        if (newName == null || "".equals(newName)){
            throw new CardioDataException("new name is empty");
        }
        if (getClientServerByName(newName) != null){
            throw new CardioDataException("server with name = " + newName + " already exists in the system");
        }
        c.setName(newName);
        return em.merge(c);
    }
}
