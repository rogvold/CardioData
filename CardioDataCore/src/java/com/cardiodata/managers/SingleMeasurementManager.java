
package com.cardiodata.managers;

import com.cardiodata.core.jpa.CardioMoodSingleMeasurement;
import com.cardiodata.exceptions.CardioDataException;
import com.cardiodata.json.ResponseConstants;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author sabir
 */
@Stateless
public class SingleMeasurementManager implements SingleMeasurementManagerLocal {
    
    @PersistenceContext(unitName = "CardioDataCorePU")
    EntityManager em;
    
    
    @Override
    public CardioMoodSingleMeasurement getSingleMeasurementById(Long mId) throws CardioDataException {
        if (mId == null){
            throw new CardioDataException("measurementId is null");
        }
        return em.find(CardioMoodSingleMeasurement.class, mId);
    }

    @Override
    public CardioMoodSingleMeasurement createSingleMeasurement(Long userId, Long serverId, String name, String description, Long creationTimestamp, String data, String dataClassName, String additionalData, String additionalDataClassName) throws CardioDataException {
        if (userId == null){
            throw new CardioDataException("userId is null");
        }
        if (serverId == null){
            throw new CardioDataException("serverId is null");
        }
        
        if (creationTimestamp == null){
            creationTimestamp = System.currentTimeMillis();
        }
        
        Long lastModificationT = 0L;
        CardioMoodSingleMeasurement m = new CardioMoodSingleMeasurement(creationTimestamp, lastModificationT, userId, serverId, data, dataClassName, additionalData, additionalDataClassName, name, description);
        return em.merge(m);
        
    }

    @Override
    public CardioMoodSingleMeasurement updateSingleMeasurement(CardioMoodSingleMeasurement newM) throws CardioDataException {
        if (newM == null){
            throw new CardioDataException("updateSingleMeasurement: newMeasurement is null");
        }
        if (newM.getUserId() == null){
            throw new CardioDataException("userId is null");
        }
        if (newM.getServerId() == null){
            throw new CardioDataException("serverId is null");
        }
        if (newM.getId() == null){
            Long ctm = (newM.getCreationTimestamp() == null) ? System.currentTimeMillis() : newM.getCreationTimestamp();
            CardioMoodSingleMeasurement m = new CardioMoodSingleMeasurement(ctm, 0L, newM.getUserId(), newM.getServerId(), newM.getData(), newM.getDataClassName(), newM.getAdditionalData(), newM.getAdditionalDataClassName(), newM.getName(), newM.getDescription());
            return em.merge(m);
        }
        CardioMoodSingleMeasurement sm = getSingleMeasurementById(newM.getId());
        
        if (sm.getLastModificationTimestamp() == null){
            sm.setLastModificationTimestamp(0L);
        }
        
        if (sm.getLastModificationTimestamp() > newM.getLastModificationTimestamp()){
            throw new CardioDataException("measurement was updated on server", ResponseConstants.SESSION_IS_MODIFIED_ON_SERVER_CODE);
        }
        
        sm.setData(newM.getData());
        sm.setDataClassName(newM.getDataClassName());
        sm.setAdditionalData(newM.getAdditionalData());
        sm.setAdditionalDataClassName(newM.getAdditionalDataClassName());
        sm.setName(newM.getName());
        sm.setDescription(newM.getDescription());
        return em.merge(sm);
    }

    @Override
    public void deleteSingleMeasurement(Long mId) throws CardioDataException {
        if (mId == null){
            throw new CardioDataException("measurementId is null");
        }
        CardioMoodSingleMeasurement m = getSingleMeasurementById(mId);
        if (m == null){
            throw new CardioDataException("Measurement with id = " + mId + " is not found");
        }
        em.remove(m);
    }

    private List<CardioMoodSingleMeasurement> getMeasurementsOfUser(Long userId, Long serverId, String className, Integer limit) throws CardioDataException{
        if (userId == null){
            throw new CardioDataException("userId is null");
        }
        Query q = em.createQuery("select c from CardioMoodSingleMeasurement c where c.userId = :userId order by c.creationTimestamp desc").setParameter("userId", userId);
        if (serverId != null){
            q = em.createQuery("select c from CardioMoodSingleMeasurement c where c.userId = :userId and c.serverId=:serverId order by c.creationTimestamp desc")
                    .setParameter("userId", userId)
                    .setParameter("serverId", serverId);
        }
        if (className != null){
            q = em.createQuery("select c from CardioMoodSingleMeasurement c where c.userId = :userId and and c.serverId=:serverId and c.dataClassName=:className order by c.creationTimestamp desc")
                    .setParameter("serverId", serverId)
                    .setParameter("className", className)
                    .setParameter("userId", userId);
        }
        if (limit != null){
            q.setMaxResults(limit);
        }
        
        List<CardioMoodSingleMeasurement> list = q.getResultList();
        if (list == null || list.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        return list;
    }
    
    @Override
    public List<CardioMoodSingleMeasurement> getMeasurementsOfUser(Long userId, Long serverId, String className) throws CardioDataException {
        return getMeasurementsOfUser(userId, serverId, className, null);
    }
    
    
    @Override
    public CardioMoodSingleMeasurement getLastSingleMeasurement(Long userId, Long serverId, String dataClassName) throws CardioDataException {
        List<CardioMoodSingleMeasurement> list = getMeasurementsOfUser(userId, serverId, dataClassName, 1);
        if (list.isEmpty()){
            return null;
        }
        return list.get(0);
    }

    @Override
    public CardioMoodSingleMeasurement renameSingleMeasurement(Long mId, String newName, String newDescription) throws CardioDataException {
        if (mId == null){
            throw new CardioDataException("measurementId is null");
        }
        CardioMoodSingleMeasurement m = getSingleMeasurementById(mId);
        if (m == null){
            throw new CardioDataException("cannot find measurement with id = " + mId);
        }
        m.setName(newName);
        m.setDescription(newDescription);
        m.setLastModificationTimestamp(System.currentTimeMillis());
        return em.merge(m);
    }


}
