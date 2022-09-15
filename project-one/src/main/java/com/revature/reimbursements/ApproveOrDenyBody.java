package com.revature.reimbursements;

public class ApproveOrDenyBody {

    private String statusId;
    private String reimbursementId;
    private String resolverId;

    public ApproveOrDenyBody() {
        super();
    }

    public ApproveOrDenyBody(String statusId, String reimbursementId, String resolverId) {
        this.statusId = statusId;
        this.reimbursementId = reimbursementId;
        this.resolverId = resolverId;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getReimbursementId() {
        return reimbursementId;
    }

    public void setReimbursementId(String reimbursementId) {
        this.reimbursementId = reimbursementId;
    }

    public String getResolverId() {
        return resolverId;
    }

    public void setResolverId(String resolverId) {
        this.resolverId = resolverId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((reimbursementId == null) ? 0 : reimbursementId.hashCode());
        result = prime * result + ((resolverId == null) ? 0 : resolverId.hashCode());
        result = prime * result + ((statusId == null) ? 0 : statusId.hashCode());
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
        if (resolverId == null) {
            if (other.resolverId != null)
                return false;
        } else if (!resolverId.equals(other.resolverId))
            return false;
        if (statusId == null) {
            if (other.statusId != null)
                return false;
        } else if (!statusId.equals(other.statusId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ApproveOrDenyBody [reimbursementId=" + reimbursementId + ", resolverId=" + resolverId + ", statusId="
                + statusId + "]";
    }

}
