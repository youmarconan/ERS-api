package com.revature.reimbursements;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Entity
@Table(name = "reimbursement_status")
@Component
public class ReimbursementStatus {

    
    @Id // indicates a primary key
    @Column(name = "id")
    private UUID statusId;

    @Column(name = "name", nullable = false, unique = true)
    private String statusName;

    public ReimbursementStatus() {
        super();
    }

    public ReimbursementStatus(UUID statusId, String statusName) {
        this.statusId = statusId;
        this.statusName = statusName;
    }

    public UUID getStatusId() {
        return statusId;
    }

    public void setStatusId(UUID statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((statusId == null) ? 0 : statusId.hashCode());
        result = prime * result + ((statusName == null) ? 0 : statusName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ReimbursementStatus other = (ReimbursementStatus) obj;
        if (statusId == null) {
            if (other.statusId != null)
                return false;
        } else if (!statusId.equals(other.statusId))
            return false;
        if (statusName == null) {
            if (other.statusName != null)
                return false;
        } else if (!statusName.equals(other.statusName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ReimbursementStatus [statusId=" + statusId + ", statusName=" + statusName + "]";
    }

    
}
