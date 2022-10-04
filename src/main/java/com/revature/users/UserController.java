package com.revature.users;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.revature.common.exceptions.AuthorizationException;


import com.revature.common.SecurityUtils;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private static Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(produces = "application/json")
    public List<UserResponse> getAllUsers(HttpServletRequest req) {

        logger.info("A GET request was received by /users at {}", LocalDateTime.now());

        HttpSession userSession = req.getSession(false);

        SecurityUtils.enforceAuthentication(userSession);
        SecurityUtils.enforcePermissions(userSession, "admin");

        return userService.getAllUsers();
    }

    @GetMapping(value = "/byId/{id}", produces = "application/json")
    public UserResponse getUserById(@PathVariable(name = "id") String id, HttpSession userSession) {

        logger.info("A GET request was received by /users/{id} at {}", LocalDateTime.now());

        SecurityUtils.enforceAuthentication(userSession);

        if (SecurityUtils.validateRole(userSession, "admin") || SecurityUtils.validateUserId(userSession, id)) {
            return userService.getUserById(id);
        } else {
            throw new AuthorizationException();
        }
    }

    @GetMapping(value = "/byUsername/{username}", produces = "application/json")
    public UserResponse getUserByUsername(@PathVariable String username, HttpSession userSession) {

        logger.info("A GET request was received by /users/{id} at {}", LocalDateTime.now());

        SecurityUtils.enforceAuthentication(userSession);

        if (SecurityUtils.validateRole(userSession, "admin")) {
            return userService.getUserByUsername(username);
        } else {
            throw new AuthorizationException();
        }
    }

    @GetMapping(value = "/byEmail/{email}", produces = "application/json")
    public UserResponse getUserByEmail(@PathVariable(name = "email") String email, HttpSession userSession) {

        logger.info("A GET request was received by /users/{id} at {}", LocalDateTime.now());

        SecurityUtils.enforceAuthentication(userSession);

        if (SecurityUtils.validateRole(userSession, "admin")) {
            return userService.getUserByEmail(email);
        } else {
            throw new AuthorizationException();
        }
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerNewUser(@RequestBody NewUserRequest requestBody, HttpServletRequest req) {

        System.out.println(requestBody);

        logger.info("A POST request was received by /users at {}", LocalDateTime.now());

        HttpSession userSession = req.getSession(false);

        SecurityUtils.enforceAuthentication(userSession);
        SecurityUtils.enforcePermissions(userSession, "admin");

        return userService.register(requestBody);
    }

    @PutMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@RequestBody UpdateRequestBody updateRequestBody, HttpServletRequest req) {

        logger.info("A PUT request was received by /users at {}", LocalDateTime.now());

        HttpSession userSession = req.getSession(false);

        SecurityUtils.enforceAuthentication(userSession);
        SecurityUtils.enforcePermissions(userSession, "admin");

        userService.updateUser(updateRequestBody);

    }

}
