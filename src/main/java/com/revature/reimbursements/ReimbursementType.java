package com.revature.reimbursements;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reimbursement_type")
public class ReimbursementType {

    @Id // indicates a primary key
    @Column(name = "id")
    private UUID typeId;

    @Column(name = "name", nullable = false, unique = true)
    private String typeName;

    public ReimbursementType() {
        super();
    }

    public ReimbursementType(UUID typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }

    public UUID getTypeId() {
        return typeId;
    }

    public void setTypeId(UUID typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((typeId == null) ? 0 : typeId.hashCode());
        result = prime * result + ((typeName == null) ? 0 : typeName.hashCode());
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
        ReimbursementType other = (ReimbursementType) obj;
        if (typeId == null) {
            if (other.typeId != null)
                return false;
        } else if (!typeId.equals(other.typeId))
            return false;
        if (typeName == null) {
            if (other.typeName != null)
                return false;
        } else if (!typeName.equals(other.typeName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ReimbursementType [typeId=" + typeId + ", typeName=" + typeName + "]";
    }


}
