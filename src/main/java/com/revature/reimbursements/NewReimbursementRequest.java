package com.revature.reimbursements;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.revature.common.Request;

@Component
public class NewReimbursementRequest implements Request<Reimbursement> {

    private String amount;
    private String description;
    private String type;

    public NewReimbursementRequest() {
        super();
    }

    public NewReimbursementRequest(String amount, String description, String type) {
        this.amount = amount;
        this.description = description;
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
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
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
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
        NewReimbursementRequest other = (NewReimbursementRequest) obj;
        if (amount == null) {
            if (other.amount != null)
                return false;
        } else if (!amount.equals(other.amount))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
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
        return "NewReimbursementRequest [amount=" + amount + ", description=" + description + ", type=" + type + "]";
    }

    @Override
    public Reimbursement extractEntity() {

        Reimbursement reimbursement = new Reimbursement();

        reimbursement.setAmount(Double.parseDouble(this.amount));
        reimbursement.setDescription(this.description);

        reimbursement.setId(UUID.randomUUID());

        return reimbursement;
    }

}
