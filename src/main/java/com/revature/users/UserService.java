package com.revature.users;

import java.util.List;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revature.common.exceptions.InvalidRequestException;
import com.revature.common.exceptions.IsAlreadyExist;
import com.revature.common.exceptions.ResourceNotFoundException;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    @Autowired
    public UserService(UserRepo userRepo, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    public List<UserResponse> getAllUsers() {
        return userRepo.findAll().stream().map(UserResponse::new).collect(Collectors.toList());
    }

    public UserResponse getUserById(String id) {
        try {
            return userRepo.findById(UUID.fromString(id))
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
            return userRepo.findUserByUsername(username)
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
            return userRepo.findUserByEmail(email)
                    .map(UserResponse::new)
                    .orElseThrow(ResourceNotFoundException::new);

        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("An invalid email was provided.");
        }
    }

    public String register(NewUserRequest newUser) {

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

        if (userRepo.existsByEmail(newUser.getEmail())) {
            throw new IsAlreadyExist("The provided email is already taken.");
        }

        if (userRepo.existsByUsername(newUser.getUsername())) {
            throw new IsAlreadyExist("The provided username is already taken.");
        }

        if (String.valueOf(newUser.getIsActive()) == null) {
            throw new InvalidRequestException("Must provid is active status!");
        }

        if (!String.valueOf(newUser.getIsActive()).equals("false")
                && !String.valueOf(newUser.getIsActive()).equals("true")) {
            throw new InvalidRequestException("Is active status must be one of (true or false)");
        }

        if (newUser.getUserRoleName() == null || newUser.getUserRoleName().toString().length() <= 0) {
            throw new InvalidRequestException("Must provid role ID!");
        }

        if (!newUser.getUserRoleName().equals("admin")
                && !newUser.getUserRoleName().equals("manager")
                && !newUser.getUserRoleName().equals("employee")) {
            throw new InvalidRequestException("invalid role name provided");
        }

        String roleId = null;

        if (newUser.getUserRoleName().equals("admin")) {
            roleId = "6e7feb50-2feb-477b-813a-3033cfdeb0b4";
        }

        if (newUser.getUserRoleName().equals("manager")) {
            roleId = "1b2ee323-f6cc-438d-9424-a2fe50ff7fc9";
        }

        if (newUser.getUserRoleName().equals("employee")) {
            roleId = "c342e80b-e53a-42e0-8942-c1fd661c6a78";
        }

        User userToPersist = newUser.extractEntity();
        UserRole role = roleRepo.findById(UUID.fromString(roleId)).orElseThrow(ResourceNotFoundException::new);
        userToPersist.setRole(role);
        userRepo.save(userToPersist);
        return "New persisted user's ID is " + userToPersist.getId();
    }

    @Transactional
    public void updateUser(UpdateRequestBody updateRequestBody) {

        // fetch the user from the database using the UserRepo by their ID (the user
        // fetched from here is in a "persistent" state)
        // throw a ResoureNotFoundException if no user is found with the provided ID
        // only update the fields provided in the UpdateRequestBody (ignore)
        // no need to call a save or update method, because the fetched user is
        // persistent and will be automagically updated when this method ends (automatic
        // dirty checking)

        System.out.println("\n" + updateRequestBody + "\n");

        User user = userRepo.findById(UUID.fromString(updateRequestBody.getUserId())).orElseThrow(ResourceNotFoundException::new);

        if (updateRequestBody.getUsername() != null) {

            if (userRepo.existsByUsername(updateRequestBody.getUsername())) {
                throw new IsAlreadyExist("Provided username is already taken");
            }
            if (updateRequestBody.getUsername().length() < 4) {
                throw new InvalidRequestException("Username must be more than 4 characters");
            }

            user.setUsername(updateRequestBody.getUsername());

        }
        if (updateRequestBody.getEmail() != null) {

            if (!userRepo.existsByEmail(updateRequestBody.getEmail())) {
                user.setEmail(updateRequestBody.getEmail());
            } else {
                throw new IsAlreadyExist("Provided Email is already taken");
            }
        }

        if (updateRequestBody.getPassword() != null) {

            if (updateRequestBody.getPassword().length() < 8) {
                throw new InvalidRequestException("A password with at least 8 characters must be provided!");
            } else {
                user.setPassword(updateRequestBody.getPassword());
            }
        }

        if (updateRequestBody.getFirstName() != null) {

            if (updateRequestBody.getFirstName().length() == 0) {
                throw new InvalidRequestException("Must provide first name");
            } else {
            user.setFirstName(updateRequestBody.getFirstName());
            }
        }

        if (updateRequestBody.getLastName() != null) {
            if (updateRequestBody.getFirstName().length() == 0) {
                throw new InvalidRequestException("Must provide last name");
            } else {
            user.setLastName(updateRequestBody.getLastName());
            }
        }

        if (updateRequestBody.getIsActive() != null) {
            if (String.valueOf(updateRequestBody.getIsActive()).equals("true")
                    || String.valueOf(updateRequestBody.getIsActive()).equals("false")) {
                user.setActive(updateRequestBody.getIsActive());
            } else {
                throw new InvalidRequestException();
            }
        }

        if (updateRequestBody.getUserRoleName() != null) {

            if (updateRequestBody.getUserRoleName().equals("admin")
                    || updateRequestBody.getUserRoleName().equals("manager")
                    || updateRequestBody.getUserRoleName().equals("employee")) {

                String roleId = null;

                if (updateRequestBody.getUserRoleName().equals("admin")) {
                    roleId = "6e7feb50-2feb-477b-813a-3033cfdeb0b4";
                }

                if (updateRequestBody.getUserRoleName().equals("manager")) {
                    roleId = "1b2ee323-f6cc-438d-9424-a2fe50ff7fc9";
                }

                if (updateRequestBody.getUserRoleName().equals("employee")) {
                    roleId = "c342e80b-e53a-42e0-8942-c1fd661c6a78";
                }

                UserRole role = roleRepo.findById(UUID.fromString(roleId))
                        .orElseThrow(ResourceNotFoundException::new);
                user.setRole(role);

            } else {
                throw new InvalidRequestException("invalid role name provided");
            }
        }

    }

}
