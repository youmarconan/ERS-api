package com.revature.users;

import java.util.List;
import java.util.stream.Collectors;

import com.revature.common.GeneratedIdResponse;
import com.revature.common.exceptions.InvalidRequestException;
import com.revature.common.exceptions.IsAlreadyExist;

public class UserService {
    
    private final UserDAO userDAO;

    public UserService (UserDAO userDAO){
        this.userDAO=userDAO;   
    }

    public List <UserResponse> getAllUsers(){
        return userDAO.allUsers().stream().map(UserResponse::new).collect(Collectors.toList());
    }

    public GeneratedIdResponse register(NewUserRequest newUser){

        if (newUser == null) {
            throw new InvalidRequestException("Provided request must not be null!");
        }

        if (newUser.getFirstName() == null || newUser.getFirstName().length() <= 0 ||
            newUser.getLastName() == null || newUser.getLastName().length() <= 0)
        {
            throw new InvalidRequestException("Must provid a first name and a last name!");
        }

        if (newUser.getEmail() == null || newUser.getEmail().length() <= 0) {
            throw new InvalidRequestException("Must provid an email!");
        }

        if (newUser.getUsername() == null || newUser.getUsername().length() < 4) {
            throw new InvalidRequestException("A username with at least 4 characters must be provided!");
        }

        if (newUser.getPassword() == null || newUser.getPassword().length() < 8) {
            throw new InvalidRequestException("A password with at least 8 characters must be provided!");
        }

        if (userDAO.isEmailTaken(newUser.getEmail())) {
            throw new IsAlreadyExist("The provided email is already taken.");
        }

        if (userDAO.isUsernameTaken(newUser.getUsername())) {
            throw new IsAlreadyExist("The provided username is already taken.");
        }

        User userToPersist = newUser.extractEntity();
        String newUserId = userDAO.register(userToPersist);
        return new GeneratedIdResponse(newUserId);
    }
}
