
package com.cardiodata.core.jpa;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author sabir
 */
@Entity
public class CardioMoodSingleMeasurement implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    protected Long creationTimestamp;
    protected Long lastModificationTimestamp;
    protected Long userId;
    protected Long serverId;
    
    @Column(length = 1000)
    protected String data;
    
    protected String dataClassName;
    
    @Column(length = 1000)
    protected String additionalData;
    
    protected String additionalDataClassName;

    protected String name;
    
    @Column(length = 1000)
    protected String description;
    
    public CardioMoodSingleMeasurement() {
    }

    public CardioMoodSingleMeasurement(Long creationTimestamp, Long lastModificationTimestamp, Long userId, Long serverId, String data, String dataClassName, String additionalData, String additionalDataClassName, String name, String description) {
        this.creationTimestamp = creationTimestamp;
        this.lastModificationTimestamp = lastModificationTimestamp;
        this.userId = userId;
        this.serverId = serverId;
        this.data = data;
        this.dataClassName = dataClassName;
        this.additionalData = additionalData;
        this.additionalDataClassName = additionalDataClassName;
        this.name = name;
        this.description = description;
    }
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
    
    public Long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public Long getLastModificationTimestamp() {
        return lastModificationTimestamp;
    }

    public void setLastModificationTimestamp(Long lastModificationTimestamp) {
        this.lastModificationTimestamp = lastModificationTimestamp;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataClassName() {
        return dataClassName;
    }

    public void setDataClassName(String dataClassName) {
        this.dataClassName = dataClassName;
    }

    public String getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }

    public String getAdditionalDataClassName() {
        return additionalDataClassName;
    }

    public void setAdditionalDataClassName(String additionalDataClassName) {
        this.additionalDataClassName = additionalDataClassName;
    }
    
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CardioMoodSingleMeasurement)) {
            return false;
        }
        CardioMoodSingleMeasurement other = (CardioMoodSingleMeasurement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cardiodata.core.jpa.CardioMoodSingleMeasurement[ id=" + id + " ]";
    }
    
}
