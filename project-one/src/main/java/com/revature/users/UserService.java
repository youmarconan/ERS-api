package com.revature.users;

import java.util.List;
import java.util.stream.Collectors;


import com.revature.common.ResponseString;
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

    public ResponseString register(NewUserRequest newUser){

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
        return new ResponseString(newUserId);
    }

    public ResponseString updateFristNmae (UpdateRequestBody updateRequestBody){

        if (updateRequestBody == null) {
            throw new InvalidRequestException("Provided request must not be null!");
        }

        if (updateRequestBody.getUpdateTo() == null || updateRequestBody.getUpdateTo().length() <= 0 ||
            updateRequestBody.getUserId() == null || updateRequestBody.getUserId().length() <= 0) {

                throw new InvalidRequestException("Must provid a first name and an user ID");
            }

        if (!userDAO.isIdValid(updateRequestBody.getUserId())){

            throw new InvalidRequestException("Must provid a valid user ID");
        }

        String updateSuccessfullMessage = userDAO.updateUserFristName(updateRequestBody.getUpdateTo(), updateRequestBody.getUserId());
        return new ResponseString(updateSuccessfullMessage);
    }

    public ResponseString updateLastNmae (UpdateRequestBody updateRequestBody){

        if (updateRequestBody == null) {
            throw new InvalidRequestException("Provided request must not be null!");
        }

        if (updateRequestBody.getUpdateTo() == null || updateRequestBody.getUpdateTo().length() <= 0 ||
            updateRequestBody.getUserId() == null || updateRequestBody.getUserId().length() <= 0) {

                throw new InvalidRequestException("Must provid a last name and an user ID");
            }

        if (!userDAO.isIdValid(updateRequestBody.getUserId())){

            throw new InvalidRequestException("Must provid a valid user ID");
        }
        
        String updateSuccessfullMessage = userDAO.updateUserLastName(updateRequestBody.getUpdateTo(), updateRequestBody.getUserId());
        return new ResponseString(updateSuccessfullMessage);
    }

    public ResponseString updateEmail (UpdateRequestBody updateRequestBody){

        if (updateRequestBody == null) {
            throw new InvalidRequestException("Provided request must not be null!");
        }

        if (updateRequestBody.getUpdateTo() == null || updateRequestBody.getUpdateTo().length() <= 0 ||
            updateRequestBody.getUserId() == null || updateRequestBody.getUserId().length() <= 0) {

                throw new InvalidRequestException("Must provid email and user ID");
            }

        if (!userDAO.isIdValid(updateRequestBody.getUserId())){

            throw new InvalidRequestException("Must provid a valid user ID");
        }

        
        String updateSuccessfullMessage = userDAO.updateUserEmail(updateRequestBody.getUpdateTo(), updateRequestBody.getUserId());
        return new ResponseString(updateSuccessfullMessage);
    }

    public ResponseString updatePassword (UpdateRequestBody updateRequestBody){

        if (updateRequestBody == null) {
            throw new InvalidRequestException("Provided request must not be null!");
        }

        if (updateRequestBody.getUpdateTo() == null || updateRequestBody.getUpdateTo().length() <= 0 ||
            updateRequestBody.getUserId() == null || updateRequestBody.getUserId().length() <= 0) {

                throw new InvalidRequestException("Must provid password and user ID");
            }

        if (!userDAO.isIdValid(updateRequestBody.getUserId())){

            throw new InvalidRequestException("Must provid a valid user ID");
        }

        if (updateRequestBody.getUpdateTo().length() < 8) {
            throw new InvalidRequestException("A password with at least 8 characters must be provided!");
        }

        
        String updateSuccessfullMessage = userDAO.updateUserPassword(updateRequestBody.getUpdateTo(), updateRequestBody.getUserId());
        return new ResponseString(updateSuccessfullMessage);
    }

    public ResponseString updateIsActive (UpdateRequestBody updateRequestBody){

        if (updateRequestBody == null) {
            throw new InvalidRequestException("Provided request must not be null!");
        }

        if (updateRequestBody.getUpdateTo() == null || updateRequestBody.getUpdateTo().length() <= 0 ||
            updateRequestBody.getUserId() == null || updateRequestBody.getUserId().length() <= 0) {

                throw new InvalidRequestException("Must provid IsActive status and user ID");
            }

        if (!userDAO.isIdValid(updateRequestBody.getUserId())){

            throw new InvalidRequestException("Must provid a valid user ID");
        }

        
        if (!updateRequestBody.getUpdateTo().equals(String.valueOf(false)) && !updateRequestBody.getUpdateTo().equals(String.valueOf(true))) {

            throw new InvalidRequestException("IsActive status must be boolean value (true/false)");
        }

        
        String updateSuccessfullMessage = userDAO.updateUserIsActive(updateRequestBody.getUpdateTo(), updateRequestBody.getUserId());
        return new ResponseString(updateSuccessfullMessage);
    }

    public ResponseString updateRoleId (UpdateRequestBody updateRequestBody){

        if (updateRequestBody == null) {
            throw new InvalidRequestException("Provided request must not be null!");
        }

        if (updateRequestBody.getUpdateTo() == null || updateRequestBody.getUpdateTo().length() <= 0 ||
            updateRequestBody.getUserId() == null || updateRequestBody.getUserId().length() <= 0) {

                throw new InvalidRequestException("Must provid role ID and user ID");
            }

        if (!userDAO.isIdValid(updateRequestBody.getUserId())){

            throw new InvalidRequestException("Must provid a valid user ID");
        }

        
        if (!updateRequestBody.getUpdateTo().equals("1") && !updateRequestBody.getUpdateTo().equals("2") && !updateRequestBody.getUpdateTo().equals("3")) {

            throw new InvalidRequestException("Role ID must be one of these numbers:\n(1)ADMIN\n(2)Manager\n(3)Employee");
        }

        String updateSuccessfullMessage = userDAO.updateUserRoleId(updateRequestBody.getUpdateTo(), updateRequestBody.getUserId());
        return new ResponseString(updateSuccessfullMessage);
    }
}
