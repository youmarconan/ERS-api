package com.revature.users;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.common.exceptions.AuthorizationException;
import com.revature.auth.AuthController;
import com.revature.common.GeneratedResponse;
import com.revature.common.SecurityUtils;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins="http://localhost:4200/", allowCredentials="true")
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


        SecurityUtils.enforceAuthentication(AuthController.userSession);
        SecurityUtils.enforcePermissions(AuthController.userSession, "admin");

        return userService.getAllUsers();
     
    }

    @GetMapping(value = "/byId/{id}", produces = "application/json")
    public UserResponse getUserById(@PathVariable(name = "id") String id) {

        logger.info("A GET request was received by /users/{id} at {}", LocalDateTime.now());

        SecurityUtils.enforceAuthentication(AuthController.userSession);

        if (SecurityUtils.validateRole(AuthController.userSession, "admin") || SecurityUtils.validateUserId(AuthController.userSession, id)) {
            return userService.getUserById(id);
        } else {
            throw new AuthorizationException();
        }
    }

    @GetMapping(value = "/byUsername/{username}", produces = "application/json")
    public UserResponse getUserByUsername(@PathVariable String username) {

        logger.info("A GET request was received by /users/{id} at {}", LocalDateTime.now());

        SecurityUtils.enforceAuthentication(AuthController.userSession);

        if (SecurityUtils.validateRole(AuthController.userSession, "admin")) {
            return userService.getUserByUsername(username);
        } else {
            throw new AuthorizationException();
        }
    }

    @GetMapping(value = "/byEmail/{email}", produces = "application/json")
    public UserResponse getUserByEmail(@PathVariable(name = "email") String email) {

        logger.info("A GET request was received by /users/{id} at {}", LocalDateTime.now());

        SecurityUtils.enforceAuthentication(AuthController.userSession);

        if (SecurityUtils.validateRole(AuthController.userSession, "admin")) {
            return userService.getUserByEmail(email);
        } else {
            throw new AuthorizationException();
        }
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public GeneratedResponse registerNewUser(@RequestBody NewUserRequest requestBody, HttpServletRequest req) {

        System.out.println(requestBody);

        logger.info("A POST request was received by /users at {}", LocalDateTime.now());

        SecurityUtils.enforceAuthentication(AuthController.userSession);
        SecurityUtils.enforcePermissions(AuthController.userSession, "admin");

        return userService.register(requestBody);
    }

    @PutMapping(consumes = "application/json")
    public void updateUser(@RequestBody UpdateRequestBody updateRequestBody, HttpServletRequest req) {

        logger.info("A PUT request was received by /users at {}", LocalDateTime.now());

        SecurityUtils.enforceAuthentication(AuthController.userSession);
        SecurityUtils.enforcePermissions(AuthController.userSession, "admin");

        userService.updateUser(updateRequestBody);

    }

}
