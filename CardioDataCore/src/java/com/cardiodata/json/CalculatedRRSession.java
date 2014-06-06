package com.cardiodata.json;

import com.cardiodata.core.jpa.CardioMoodSession;
import java.util.Map;

/**
 *
 * @author sabir
 */
public class CalculatedRRSession extends CardioMoodSession{
    
    protected Map<String, double[][]> calculatedParameters;
    
    public CalculatedRRSession(CardioMoodSession cs, Map<String, double[][]> parameters){
        super(cs.getId(), cs.getName(), cs.getDescription(), cs.getServerId(), cs.getUserId(), cs.getCreationTimestamp(), cs.getEndTimestamp(), cs.getDataClassName(), cs.getOriginalSessionId(), cs.getLastModificationTimestamp());
        this.calculatedParameters = parameters;
    }

    public CalculatedRRSession() {
        super();
    }

}
