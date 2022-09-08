package com.revature.auth;

import com.revature.common.exceptions.AuthenticationException;
import com.revature.common.exceptions.InvalidRequestException;
import com.revature.users.User;
import com.revature.users.UserDAO;

public class AuthService {
    
    private final UserDAO userDAO;

    public AuthService (UserDAO userDAO){
        this.userDAO=userDAO;   
    }

    public User authenticate (Credentials credentials){
        
        if (credentials == null) {
            throw new InvalidRequestException("The provided credentials object must not be null!");
        }

        if (credentials.getUsername().length() < 4) {
            throw new InvalidRequestException("The provided username must be at least 4 characters!");
        }

        if (credentials.getPassword().length() < 8) {
            throw new InvalidRequestException("The provided password must be at least 8 characters!");
        }

        return userDAO.login(credentials.getUsername(), credentials.getPassword()).orElseThrow(AuthenticationException::new);

    }
}
