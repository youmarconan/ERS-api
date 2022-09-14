package com.revature.reimbursements;

import com.revature.common.Request;

public class NewReimbursementRequest implements Request<Reimbursement> {

    private double amount;
    private String description;
    private String authorId;
    private String typeId;

    
    public NewReimbursementRequest() {
        super();
    }


    public NewReimbursementRequest(double amount, String description, String authorId, String typeId) {
        this.amount = amount;
        this.description = description;
        this.authorId = authorId;
        this.typeId = typeId;
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
        result = prime * result + ((typeId == null) ? 0 : typeId.hashCode());
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
        if (typeId == null) {
            if (other.typeId != null)
                return false;
        } else if (!typeId.equals(other.typeId))
            return false;
        return true;
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


    public String getAuthorId() {
        return authorId;
    }


    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }


    public String getTypeId() {
        return typeId;
    }


    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }


    @Override
    public String toString() {
        return "NewReimbursementRequest [amount=" + amount + ", authorId=" + authorId + ", description=" + description
                + ", typeId=" + typeId + "]";
    }


    @Override
    public Reimbursement extractEntity() {

        Reimbursement reimbursement = new Reimbursement();
        ReimbursementType reimbursementType = new ReimbursementType();

        reimbursement.setAmount(this.amount);
        reimbursement.setDescription(this.description);
        reimbursement.setAuthorId(this.authorId);

        reimbursementType.setTypeId(this.typeId);

        reimbursement.setType(reimbursementType);

        return reimbursement;
    }
    
}
