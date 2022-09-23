package com.revature.reimbursements;

import java.io.Serializable;

public class ReimbursementResponse implements Serializable {

    private String id;
    private double amount;
    private String submitted;
    private String resolved;
    private String description;
    private String authorId;
    private String resolverId;
    private String typeNmae;
    private String statusName;

    public ReimbursementResponse() {
    }

    public ReimbursementResponse(Reimbursement reimbursement ) {
        this.id = reimbursement.getId().toString();
        this.amount = reimbursement.getAmount();
        this.submitted = reimbursement.getSubmitted().toString();
        this.resolved = reimbursement.getResolved().toString();
        this.description = reimbursement.getDescription();
        this.authorId = reimbursement.getAuthorId().toString();
        this.resolverId = reimbursement.getResolverId().toString();
        this.typeNmae = reimbursement.getType().getTypeName();
        this.statusName = reimbursement.getStatus().getStatusName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getSubmitted() {
        return submitted;
    }

    public void setSubmitted(String submitted) {
        this.submitted = submitted;
    }

    public String getResolved() {
        return resolved;
    }

    public void setResolved(String resolved) {
        this.resolved = resolved;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getResolverId() {
        return resolverId;
    }

    public void setResolverId(String resolverId) {
        this.resolverId = resolverId;
    }

    public String getTypeNmae() {
        return typeNmae;
    }

    public void setTypeNmae(String typeNmae) {
        this.typeNmae = typeNmae;
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
        long temp;
        temp = Double.doubleToLongBits(amount);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((authorId == null) ? 0 : authorId.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((resolved == null) ? 0 : resolved.hashCode());
        result = prime * result + ((resolverId == null) ? 0 : resolverId.hashCode());
        result = prime * result + ((statusName == null) ? 0 : statusName.hashCode());
        result = prime * result + ((submitted == null) ? 0 : submitted.hashCode());
        result = prime * result + ((typeNmae == null) ? 0 : typeNmae.hashCode());
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
        ReimbursementResponse other = (ReimbursementResponse) obj;
        if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
            return false;
        if (authorId == null) {
            if (other.authorId != null)
                return false;
        } else if (!authorId.equals(other.authorId))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (resolved == null) {
            if (other.resolved != null)
                return false;
        } else if (!resolved.equals(other.resolved))
            return false;
        if (resolverId == null) {
            if (other.resolverId != null)
                return false;
        } else if (!resolverId.equals(other.resolverId))
            return false;
        if (statusName == null) {
            if (other.statusName != null)
                return false;
        } else if (!statusName.equals(other.statusName))
            return false;
        if (submitted == null) {
            if (other.submitted != null)
                return false;
        } else if (!submitted.equals(other.submitted))
            return false;
        if (typeNmae == null) {
            if (other.typeNmae != null)
                return false;
        } else if (!typeNmae.equals(other.typeNmae))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ReimbursementResponse [amount=" + amount + ", authorId=" + authorId + ", description=" + description
                + ", id=" + id + ", resolved=" + resolved + ", resolverId=" + resolverId + ", statusName=" + statusName
                + ", submitted=" + submitted + ", typeNmae=" + typeNmae + "]";
    }

}
