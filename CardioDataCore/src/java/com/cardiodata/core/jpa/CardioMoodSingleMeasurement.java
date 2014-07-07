package com.cardiodata.core.jpa;

import com.cardiodata.json.AerobicThresholdAdditionalData;
import com.cardiodata.json.AerobicThresholdData;
import com.cardiodata.json.AnaerobicThresholdAdditionalData;
import com.cardiodata.json.AnaerobicThresholdData;
import com.cardiodata.json.HeightAdditionalData;
import com.cardiodata.json.HeightData;
import com.cardiodata.json.WeightAdditionalData;
import com.cardiodata.json.WeightData;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
    protected String dataItem;

    @Enumerated(EnumType.STRING)
    protected SingleMeasurementDataType dataType;

    @Column(length = 1000)
    protected String additionalDataItem;

    @Enumerated(EnumType.STRING)
    protected SingleMeasurementAdditionalDataType additionalDataType;

    protected String name;

    @Column(length = 1000)
    protected String description;

    public CardioMoodSingleMeasurement(Long creationTimestamp, Long lastModificationTimestamp, Long userId, Long serverId, String dataItem, SingleMeasurementDataType dataType, String additionalDataItem, SingleMeasurementAdditionalDataType additionalDataType, String name, String description) {
        this.creationTimestamp = creationTimestamp;
        this.lastModificationTimestamp = lastModificationTimestamp;
        this.userId = userId;
        this.serverId = serverId;
        this.dataItem = dataItem;
        this.dataType = dataType;
        this.additionalDataItem = additionalDataItem;
        this.additionalDataType = additionalDataType;
        this.name = name;
        this.description = description;
    }

    public CardioMoodSingleMeasurement() {
        
    }

    

    public enum SingleMeasurementAdditionalDataType {
        WEIGHT(WeightAdditionalData.class), HEIGHT(HeightAdditionalData.class), AEROBIC_THRESHOLD(AerobicThresholdAdditionalData.class), ANAEROBIC_THRESHOLD(AnaerobicThresholdAdditionalData.class);
        
        private Class clazz;
        
        public Class getDeserializationClass(){
            return this.clazz;
        }
        
        private SingleMeasurementAdditionalDataType(Class clazz){
            this.clazz = clazz;
        }

    }
    
    public enum SingleMeasurementDataType {
        WEIGHT(WeightData.class), HEIGHT(HeightData.class), AEROBIC_THRESHOLD(AerobicThresholdData.class), ANAEROBIC_THRESHOLD(AnaerobicThresholdData.class);
        
        private Class clazz;
        
        public Class getDeserializationClass(){
            return this.clazz;
        }
        
        private SingleMeasurementDataType(Class clazz){
            this.clazz = clazz;
        }

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

    public String getDataItem() {
        return dataItem;
    }

    public void setDataItem(String dataItem) {
        this.dataItem = dataItem;
    }

    public SingleMeasurementDataType getDataType() {
        return dataType;
    }

    public void setDataType(SingleMeasurementDataType dataType) {
        this.dataType = dataType;
    }

    public String getAdditionalDataItem() {
        return additionalDataItem;
    }

    public void setAdditionalDataItem(String additionalDataItem) {
        this.additionalDataItem = additionalDataItem;
    }

    public SingleMeasurementAdditionalDataType getAdditionalDataType() {
        return additionalDataType;
    }

    public void setAdditionalDataType(SingleMeasurementAdditionalDataType additionalDataType) {
        this.additionalDataType = additionalDataType;
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
