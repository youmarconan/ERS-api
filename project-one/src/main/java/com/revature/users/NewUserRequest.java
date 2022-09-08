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
    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean isActive) {
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
