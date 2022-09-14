package com.revature.users;

public class UpdateRequestBody {

    private String updateTo;
    private String userId;

    
    public UpdateRequestBody() {
        super();
    }

    
    public UpdateRequestBody(String updateTo, String userId) {
        this.updateTo = updateTo;
        this.userId = userId;
    }


    public String getUpdateTo() {
        return updateTo;
    }

    public void setUpdateTo(String updateTo) {
        this.updateTo = updateTo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((updateTo == null) ? 0 : updateTo.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
        UpdateRequestBody other = (UpdateRequestBody) obj;
        if (updateTo == null) {
            if (other.updateTo != null)
                return false;
        } else if (!updateTo.equals(other.updateTo))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "UpdateRequestBody [updateTo=" + updateTo + ", userId=" + userId + "]";
    }

}
