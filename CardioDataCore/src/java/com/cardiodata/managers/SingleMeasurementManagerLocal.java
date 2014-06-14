
package com.cardiodata.managers;

import com.cardiodata.core.jpa.CardioMoodSingleMeasurement;
import com.cardiodata.exceptions.CardioDataException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author sabir
 */
@Local
public interface SingleMeasurementManagerLocal {
    
    public CardioMoodSingleMeasurement getSingleMeasurementById(Long mId) throws CardioDataException;
    
    public CardioMoodSingleMeasurement createSingleMeasurement(Long userId, Long serverId, String name, String description, Long creationTimestamp, String data, String dataClassName, String additionalData, String additionalDataClassName) throws CardioDataException;
    
    public CardioMoodSingleMeasurement updateSingleMeasurement(CardioMoodSingleMeasurement newM) throws CardioDataException;
    
    public CardioMoodSingleMeasurement renameSingleMeasurement(Long mId, String newName, String newDescription) throws CardioDataException;
    
    public void deleteSingleMeasurement(Long mId) throws CardioDataException;
    
    public List<CardioMoodSingleMeasurement> getMeasurementsOfUser(Long userId, Long serverId, String dataClassName) throws CardioDataException;
    
    public CardioMoodSingleMeasurement getLastSingleMeasurement(Long userId, Long serverId, String dataClassName) throws CardioDataException;
    
}
