package com.revature.users;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.revature.common.Request;

public class NewUserRequest implements Request<User> {

    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    @JsonProperty("isActive")
    private Boolean isActive;
    private String userRoleName;

    public NewUserRequest() {
        super();
    }

    public NewUserRequest(String username, String email, String password, String firstName, String lastName,
            Boolean isActive, String userRoleName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.userRoleName = userRoleName;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getUserRoleName() {
        return userRoleName;
    }

    public void setUserRoleName(String userRoleName) {
        this.userRoleName = userRoleName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((isActive == null) ? 0 : isActive.hashCode());
        result = prime * result + ((userRoleName == null) ? 0 : userRoleName.hashCode());
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
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (isActive == null) {
            if (other.isActive != null)
                return false;
        } else if (!isActive.equals(other.isActive))
            return false;
        if (userRoleName == null) {
            if (other.userRoleName != null)
                return false;
        } else if (!userRoleName.equals(other.userRoleName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "NewUserRequest [username=" + username + ", email=" + email + ", password=" + password + ", firstName="
                + firstName + ", lastName=" + lastName + ", isActive=" + isActive + ", userRoleName=" + userRoleName
                + "]";
    }

    @Override
    public User extractEntity() {

        User extractedUser = new User();
        extractedUser.setUsername(this.username);
        extractedUser.setEmail(this.email);
        extractedUser.setPassword(this.password);
        extractedUser.setFirstName(this.firstName);
        extractedUser.setLastName(this.lastName);
        extractedUser.setActive(this.isActive);

        extractedUser.setId(UUID.randomUUID());

        return extractedUser;
    }

}
