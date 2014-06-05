package com.cardiodata.json;

import com.cardiodata.core.jpa.CardioMoodSession;
import java.util.Map;

/**
 *
 * @author sabir
 */
public class CalculatedRRSession extends CardioMoodSession{
    
    protected Map<String, double[][]> calculatedParameters;

    public CalculatedRRSession(Map<String, double[][]> calculatedParameters, Long id, String name, String description, Long serverId, Long userId, Long creationTimestamp, String dataClassName, Long originalSessionId, Long lastModificationTimestamp) {
        super(id, name, description, serverId, userId, creationTimestamp, dataClassName, originalSessionId, lastModificationTimestamp);
        this.calculatedParameters = calculatedParameters;
    }
    
    public CalculatedRRSession(CardioMoodSession cs, Map<String, double[][]> parameters){
        super(cs.getId(), cs.getName(), cs.getDescription(), cs.getServerId(), cs.getUserId(), cs.getCreationTimestamp(), cs.getDataClassName(), cs.getOriginalSessionId(), cs.getLastModificationTimestamp());
        this.calculatedParameters = parameters;
    }

    public CalculatedRRSession() {
        super();
    }

}
