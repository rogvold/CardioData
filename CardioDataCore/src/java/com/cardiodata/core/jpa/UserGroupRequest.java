package com.cardiodata.core.jpa;

import com.cardiodata.enums.UserGroupRequestStatusEnum;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
@Entity
public class UserGroupRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    private Long invitorId;
    private Long groupId;
    @Enumerated(EnumType.STRING)
    private UserGroupRequestStatusEnum requestStatus;

    public UserGroupRequest() {
    }

    public UserGroupRequest(Long userId, Long invitorId, Long groupId, UserGroupRequestStatusEnum requestStatus) {
        this.userId = userId;
        this.invitorId = invitorId;
        this.groupId = groupId;
        this.requestStatus = requestStatus;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getInvitorId() {
        return invitorId;
    }

    public void setInvitorId(Long invitorId) {
        this.invitorId = invitorId;
    }

    public UserGroupRequestStatusEnum getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(UserGroupRequestStatusEnum requestStatus) {
        this.requestStatus = requestStatus;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserGroupRequest)) {
            return false;
        }
        UserGroupRequest other = (UserGroupRequest) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cardiodata.core.jpa.UserGroupRequest[ id=" + id + " ]";
    }
}
