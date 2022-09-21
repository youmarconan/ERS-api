package com.revature.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.revature.common.Request;

public class NewUserRequest implements Request<User> {
    
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    @JsonProperty("isActive")
    private boolean isActive;
    private String userRoleId;
    
    public NewUserRequest() {
        super();
    }

    
    public NewUserRequest(String username, String email, String password, String firstName, String lastName,
            boolean isActive, String userRoleId) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.userRoleId = userRoleId;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + (isActive ? 1231 : 1237);
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((userRoleId == null) ? 0 : userRoleId.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
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
        NewUserRequest other = (NewUserRequest) obj;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (isActive != other.isActive)
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (userRoleId == null) {
            if (other.userRoleId != null)
                return false;
        } else if (!userRoleId.equals(other.userRoleId))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    public String getUserRoleId() {
        return userRoleId;
    }
    public void setUserRoleId(String userRoleId) {
        this.userRoleId = userRoleId;
    }
    @Override
    public String toString() {
        return "NewUserRequest [email=" + email + ", firstName=" + firstName + ", isActive=" + isActive + ", lastName="
                + lastName + ", password=" + password + ", userRoleId=" + userRoleId + ", username=" + username + "]";
    }
    @Override
    public User extractEntity() {

        User extractedUser = new User();
        UserRole extractedRoleId = new UserRole();

        extractedUser.setUsername(this.username);
        extractedUser.setEmail(this.email);
        extractedUser.setPassword(this.password);
        extractedUser.setFirstName(this.firstName);
        extractedUser.setLastName(this.lastName);
        extractedUser.setIsActive(this.isActive);

        extractedRoleId.setId(this.userRoleId);

        extractedUser.setRole(extractedRoleId);

        return extractedUser;
    }

    
}
