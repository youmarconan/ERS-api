package com.revature.reimbursements;

public class UpdateOwnReimbBody {

    private String updateTo;
    private String reimbursementId;

    public UpdateOwnReimbBody() {
    }

    public UpdateOwnReimbBody(String updateTo, String reimbursementId) {
        this.updateTo = updateTo;
        this.reimbursementId = reimbursementId;
    }

    public String getUpdateTo() {
        return updateTo;
    }

    public void setUpdateTo(String updateTo) {
        this.updateTo = updateTo;
    }

    public String getReimbursementId() {
        return reimbursementId;
    }

    public void setReimbursementId(String reimbursementId) {
        this.reimbursementId = reimbursementId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((reimbursementId == null) ? 0 : reimbursementId.hashCode());
        result = prime * result + ((updateTo == null) ? 0 : updateTo.hashCode());
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
        if (reimbursementId == null) {
            if (other.reimbursementId != null)
                return false;
        } else if (!reimbursementId.equals(other.reimbursementId))
            return false;
        if (updateTo == null) {
            if (other.updateTo != null)
                return false;
        } else if (!updateTo.equals(other.updateTo))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "UpdateOwnReimbBody [reimbursementId=" + reimbursementId + ", updateTo=" + updateTo + "]";
    }

}
