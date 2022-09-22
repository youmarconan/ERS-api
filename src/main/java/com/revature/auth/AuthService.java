package com.revature.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.common.exceptions.AuthenticationException;
import com.revature.common.exceptions.InvalidRequestException;
import com.revature.users.UserDAO;
import com.revature.users.UserResponse;

@Service
public class AuthService {
    
    private final UserDAO userDAO;

    @Autowired
    public AuthService (UserDAO userDAO){
        this.userDAO=userDAO;   
    }

    public UserResponse authenticate (Credentials credentials){
        
        if (credentials == null) {
            throw new InvalidRequestException("The provided credentials object must not be null!");
        }

        if (credentials.getUsername().length() < 4) {
            throw new InvalidRequestException("The provided username must be at least 4 characters!");
        }

        if (credentials.getPassword().length() < 8) {
            throw new InvalidRequestException("The provided password must be at least 8 characters!");
        }

        UserResponse userResponse = userDAO.login(credentials).map(UserResponse::new).orElseThrow(AuthenticationException::new);

        if(userResponse.isActive()){
            return userResponse;
        }else{
            throw new AuthenticationException();
        }
    }
}
