package com.revature.reimbursements;

import org.springframework.stereotype.Component;

@Component
public class UpdateOwnReimbBody {

    private String reimbursementId;
    private double amount;
    private String description;
    private String type;

    public UpdateOwnReimbBody() {
    }

    public UpdateOwnReimbBody(String reimbursementId, double amount, String description, String type) {
        this.reimbursementId = reimbursementId;
        this.amount = amount;
        this.description = description;
        this.type = type;
    }

    public String getReimbursementId() {
        return reimbursementId;
    }

    public void setReimbursementId(String reimbursementId) {
        this.reimbursementId = reimbursementId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(amount);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((reimbursementId == null) ? 0 : reimbursementId.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        UpdateOwnReimbBody other = (UpdateOwnReimbBody) obj;
        if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (reimbursementId == null) {
            if (other.reimbursementId != null)
                return false;
        } else if (!reimbursementId.equals(other.reimbursementId))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "UpdateOwnReimbBody [amount=" + amount + ", description=" + description + ", reimbursementId="
                + reimbursementId + ", type=" + type + "]";
    }

    

}
