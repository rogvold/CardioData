package com.cardiodata.json;

import com.cardiodata.core.jpa.User;
import java.util.List;

/**
 *
 * @author sabir
 */
public class DashboardUser extends User {
    
    protected Double bpm;
    protected Double SDNN;

    protected Long lastUpdatedTimestamp;
    protected List<Double> lastIntervals;

    public DashboardUser(Double bpm, Double SDNN, Long lastUpdatedTimestamp, List<Double> lastIntervals) {
        super();
        this.bpm = bpm;
        this.SDNN = SDNN;
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
        this.lastIntervals = lastIntervals;
    }

    
    public DashboardUser(Double bpm, Double SDNN) {
        super();
        this.bpm = bpm;
        this.SDNN = SDNN;
    }

    public DashboardUser() {
        super();
    }
    
    
    public Double getBpm() {
        return bpm;
    }

    public void setBpm(Double bpm) {
        this.bpm = bpm;
    }

    public Double getSDNN() {
        return SDNN;
    }

    public void setSDNN(Double SDNN) {
        this.SDNN = SDNN;
    }
    
    public Long getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public void setLastUpdatedTimestamp(Long lastUpdatedTimestamp) {
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
    }

    public List<Double> getLastIntervals() {
        return lastIntervals;
    }

    public void setLastIntervals(List<Double> lastIntervals) {
        this.lastIntervals = lastIntervals;
    }
    
    
}