

package com.cardiodata.json;

import com.cardiodata.core.jpa.User;

/**
 *
 * @author sabir
 */
public class DashboardUser extends User {
    
    protected Double bpm;
    protected Double SDNN;

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
    
    
}
