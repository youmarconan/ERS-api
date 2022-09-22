package com.revature.users;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.common.ResponseString;
import com.revature.common.exceptions.InvalidRequestException;
import com.revature.common.exceptions.IsAlreadyExist;
import com.revature.common.exceptions.ResourceNotFoundException;

@Service
public class UserService {

    private final UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public List<UserResponse> getAllUsers() {
        return userDAO.allUsers().stream().map(UserResponse::new).collect(Collectors.toList());
    }

    public UserResponse getUserById(String id) {

        if (id == null || id.length() <= 0) {
            throw new InvalidRequestException("A non-empty ID must be provided!");
        }

        try {
            return userDAO.findUserById(id)
                    .map(UserResponse::new)
                    .orElseThrow(ResourceNotFoundException::new);

        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("An invalid ID was provided.");
        }
    }

    public UserResponse getUserByUsername(String username) {

        if (username == null || username.length() <= 0) {
            throw new InvalidRequestException("A non-empty username must be provided!");
        }

        try {
            return userDAO.findUserByUsername(username)
                    .map(UserResponse::new)
                    .orElseThrow(ResourceNotFoundException::new);

        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("An invalid username was provided.");
        }
    }

    public UserResponse getUserByEmail(String email) {

        if (email == null || email.length() <= 0) {
            throw new InvalidRequestException("A non-empty email must be provided!");
        }

        try {
            return userDAO.findUserByEmail(email)
                    .map(UserResponse::new)
                    .orElseThrow(ResourceNotFoundException::new);

        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("An invalid email was provided.");
        }
    }

    public ResponseString register(NewUserRequest newUser) {

        if (newUser == null) {
            throw new InvalidRequestException("Provided request must not be null!");
        }

        if (newUser.getFirstName() == null || newUser.getFirstName().length() <= 0 ||
                newUser.getLastName() == null || newUser.getLastName().length() <= 0) {
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

        if (String.valueOf(newUser.getIsActive()) == null || String.valueOf(newUser.getIsActive()).length() <= 0) {
            throw new InvalidRequestException("Must provid is active status!");
        }

        if (!String.valueOf(newUser.getIsActive()).equals("false")
                && !String.valueOf(newUser.getIsActive()).equals("true")) {
            throw new InvalidRequestException("Is active status must be one of (true or false)");
        }

        if (newUser.getUserRoleId() == null || newUser.getUserRoleId().length() <= 0) {
            throw new InvalidRequestException("Must provid role ID!");
        }

        if (!newUser.getUserRoleId().equals("1") && !newUser.getUserRoleId().equals("2")
                && !newUser.getUserRoleId().equals("3")) {
            throw new InvalidRequestException("Role ID must be one of (1 or 2 or 3)");
        }

        User userToPersist = newUser.extractEntity();
        String newUserId = userDAO.register(userToPersist);
        return new ResponseString(newUserId);
    }

    public ResponseString updateFristNmae(UpdateRequestBody updateRequestBody) {

        if (updateRequestBody == null) {
            throw new InvalidRequestException("Provided request must not be null!");
        }

        if (updateRequestBody.getUpdateTo() == null || updateRequestBody.getUpdateTo().length() <= 0 ||
                updateRequestBody.getUserId() == null || updateRequestBody.getUserId().length() <= 0) {

            throw new InvalidRequestException("Must provid first name and user ID");
        }

        if (!userDAO.isIdValid(updateRequestBody.getUserId())) {

            throw new InvalidRequestException("Must provid a valid user ID");
        }

        String updateSuccessfullMessage = userDAO.updateUserFristName(updateRequestBody.getUpdateTo(),
                updateRequestBody.getUserId());
        return new ResponseString(updateSuccessfullMessage);
    }

    public ResponseString updateLastNmae(UpdateRequestBody updateRequestBody) {

        if (updateRequestBody == null) {
            throw new InvalidRequestException("Provided request must not be null!");
        }

        if (updateRequestBody.getUpdateTo() == null || updateRequestBody.getUpdateTo().length() <= 0 ||
                updateRequestBody.getUserId() == null || updateRequestBody.getUserId().length() <= 0) {

            throw new InvalidRequestException("Must provid a last name and an user ID");
        }

        if (!userDAO.isIdValid(updateRequestBody.getUserId())) {

            throw new InvalidRequestException("Must provid a valid user ID");
        }

        String updateSuccessfullMessage = userDAO.updateUserLastName(updateRequestBody.getUpdateTo(),
                updateRequestBody.getUserId());
        return new ResponseString(updateSuccessfullMessage);
    }

    public ResponseString updateEmail(UpdateRequestBody updateRequestBody) {

        if (updateRequestBody == null) {
            throw new InvalidRequestException("Provided request must not be null!");
        }

        if (updateRequestBody.getUpdateTo() == null || updateRequestBody.getUpdateTo().length() <= 0 ||
                updateRequestBody.getUserId() == null || updateRequestBody.getUserId().length() <= 0) {

            throw new InvalidRequestException("Must provid email and user ID");
        }

        if (!userDAO.isIdValid(updateRequestBody.getUserId())) {

            throw new InvalidRequestException("Must provid a valid user ID");
        }

        String updateSuccessfullMessage = userDAO.updateUserEmail(updateRequestBody.getUpdateTo(),
                updateRequestBody.getUserId());
        return new ResponseString(updateSuccessfullMessage);
    }

    public ResponseString updatePassword(UpdateRequestBody updateRequestBody) {

        if (updateRequestBody == null) {
            throw new InvalidRequestException("Provided request must not be null!");
        }

        if (updateRequestBody.getUpdateTo() == null || updateRequestBody.getUpdateTo().length() <= 0 ||
                updateRequestBody.getUserId() == null || updateRequestBody.getUserId().length() <= 0) {

            throw new InvalidRequestException("Must provid password and user ID");
        }

        if (!userDAO.isIdValid(updateRequestBody.getUserId())) {

            throw new InvalidRequestException("Must provid a valid user ID");
        }

        if (updateRequestBody.getUpdateTo().length() < 8) {
            throw new InvalidRequestException("A password with at least 8 characters must be provided!");
        }

        String updateSuccessfullMessage = userDAO.updateUserPassword(updateRequestBody.getUpdateTo(),
                updateRequestBody.getUserId());
        return new ResponseString(updateSuccessfullMessage);
    }

    public ResponseString updateIsActive(UpdateRequestBody updateRequestBody) {

        if (updateRequestBody == null) {
            throw new InvalidRequestException("Provided request must not be null!");
        }

        if (updateRequestBody.getUpdateTo() == null || updateRequestBody.getUpdateTo().length() <= 0 ||
                updateRequestBody.getUserId() == null || updateRequestBody.getUserId().length() <= 0) {

            throw new InvalidRequestException("Must provid IsActive status and user ID");
        }

        if (!userDAO.isIdValid(updateRequestBody.getUserId())) {

            throw new InvalidRequestException("Must provid a valid user ID");
        }

        if (!updateRequestBody.getUpdateTo().equals(String.valueOf(false))
                && !updateRequestBody.getUpdateTo().equals(String.valueOf(true))) {

            throw new InvalidRequestException("IsActive status must be boolean value (true/false)");
        }

        String updateSuccessfullMessage = userDAO.updateUserIsActive(updateRequestBody.getUpdateTo(),
                updateRequestBody.getUserId());
        return new ResponseString(updateSuccessfullMessage);
    }

    public ResponseString updateRoleId(UpdateRequestBody updateRequestBody) {

        if (updateRequestBody == null) {
            throw new InvalidRequestException("Provided request must not be null!");
        }

        if (updateRequestBody.getUpdateTo() == null || updateRequestBody.getUpdateTo().length() <= 0 ||
                updateRequestBody.getUserId() == null || updateRequestBody.getUserId().length() <= 0) {

            throw new InvalidRequestException("Must provid role ID and user ID");
        }

        if (!userDAO.isIdValid(updateRequestBody.getUserId())) {

            throw new InvalidRequestException("Must provid a valid user ID");
        }

        if (!updateRequestBody.getUpdateTo().equals("1") && !updateRequestBody.getUpdateTo().equals("2")
                && !updateRequestBody.getUpdateTo().equals("3")) {

            throw new InvalidRequestException(
                    "Invalid role ID, Role ID must be one of these numbers: (1)ADMIN (2)Manager (3)Employee");
        }

        String updateSuccessfullMessage = userDAO.updateUserRoleId(updateRequestBody.getUpdateTo(),
                updateRequestBody.getUserId());
        return new ResponseString(updateSuccessfullMessage);
    }
}
