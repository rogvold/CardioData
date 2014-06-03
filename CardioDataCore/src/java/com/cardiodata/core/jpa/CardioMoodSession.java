package com.cardiodata.core.jpa;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
@Entity
@Table(name="cardiosession")
public class CardioMoodSession implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    protected String name;
    @Column(length = 1000)
    protected String description;
    protected Long serverId;
    protected Long userId;
    protected Long creationTimestamp;
    protected Long endTimestamp;
    protected String dataClassName;
    protected Long lastModificationTimestamp;
    protected Long originalSessionId;

    public CardioMoodSession(Long id, String name, String description, Long serverId, Long userId, Long creationTimestamp, String dataClassName, Long originalSessionId, Long lastModificationTimestamp) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.serverId = serverId;
        this.userId = userId;
        this.creationTimestamp = creationTimestamp;
        this.dataClassName = dataClassName;
        this.originalSessionId = originalSessionId;
    }

    public CardioMoodSession() {
    }

    public String getDataClassName() {
        return dataClassName;
    }

    public void setDataClassName(String dataClassName) {
        this.dataClassName = dataClassName;
    }

    public Long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLastModificationTimestamp() {
        return lastModificationTimestamp;
    }

    public void setLastModificationTimestamp(Long lastModificationTimestamp) {
        this.lastModificationTimestamp = lastModificationTimestamp;
    }

    public Long getOriginalSessionId() {
        return originalSessionId;
    }

    public void setOriginalSessionId(Long originSessionId) {
        this.originalSessionId = originSessionId;
    }

    public Long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(Long endTimestamp) {
        this.endTimestamp = endTimestamp;
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
        if (!(object instanceof CardioMoodSession)) {
            return false;
        }
        CardioMoodSession other = (CardioMoodSession) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cardiodata.core.jpa.CardioMoodSession[ id=" + id + " ]";
    }
}
