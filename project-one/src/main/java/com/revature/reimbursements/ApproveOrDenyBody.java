package com.revature.reimbursements;

public class ApproveOrDenyBody {

    private String statusName;
    private String reimbursementId;
    public ApproveOrDenyBody() {
        super();
    }
    public ApproveOrDenyBody(String statusName, String reimbursementId) {
        this.statusName = statusName;
        this.reimbursementId = reimbursementId;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
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
        ApproveOrDenyBody other = (ApproveOrDenyBody) obj;
        if (reimbursementId == null) {
            if (other.reimbursementId != null)
                return false;
        } else if (!reimbursementId.equals(other.reimbursementId))
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
        return "ApproveOrDenyBody [reimbursementId=" + reimbursementId + ", statusName=" + statusName + "]";
    }
    

}
