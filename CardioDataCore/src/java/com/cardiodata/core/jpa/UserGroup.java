package com.cardiodata.core.jpa;

import com.cardiodata.enums.UserGroupPrivecyEnum;
import com.cardiodata.enums.UserGroupStatusEnum;
import com.cardiodata.enums.UserGroupTypeEnum;
import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
@Entity
public class UserGroup implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(length = 2000)
    private String description;
    private Long creationTimestamp;
    private Long ownerId;
    @Enumerated(EnumType.STRING)
    private UserGroupTypeEnum groupType;
    @Enumerated(EnumType.STRING)
    private UserGroupStatusEnum groupStatus;
    @Enumerated(EnumType.STRING)
    private UserGroupPrivecyEnum groupPrivacy;

    public UserGroup(String name, String description, Long creationTimestamp, Long ownerId, UserGroupTypeEnum groupType, UserGroupStatusEnum groupStatus, UserGroupPrivecyEnum groupPrivacy) {
        this.name = name;
        this.description = description;
        this.creationTimestamp = creationTimestamp;
        this.ownerId = ownerId;
        this.groupType = groupType;
        this.groupStatus = groupStatus;
        this.groupPrivacy = groupPrivacy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public UserGroupPrivecyEnum getGroupPrivacy() {
        return groupPrivacy;
    }

    public void setGroupPrivacy(UserGroupPrivecyEnum groupPrivacy) {
        this.groupPrivacy = groupPrivacy;
    }

    public UserGroupStatusEnum getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(UserGroupStatusEnum groupStatus) {
        this.groupStatus = groupStatus;
    }

    public UserGroupTypeEnum getGroupType() {
        return groupType;
    }

    public void setGroupType(UserGroupTypeEnum groupType) {
        this.groupType = groupType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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
        if (!(object instanceof UserGroup)) {
            return false;
        }
        UserGroup other = (UserGroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cardiodata.core.jpa.UserGroup[ id=" + id + " ]";
    }
}
