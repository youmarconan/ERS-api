package com.revature.auth;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

import com.revature.common.exceptions.InvalidRequestException;
import com.revature.users.UserResponse;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins="http://localhost:4200/", allowCredentials="true")
public class AuthController {

    private final AuthService authService;

    public static HttpSession userSession;

    private static Logger logger = LogManager.getLogger(AuthController.class);

    

    public AuthController() {
        this.authService = null;
    }

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public UserResponse authenticate(@RequestBody Credentials credentials, HttpServletRequest req) {


        if (credentials == null) {
            throw new InvalidRequestException("The provided credentials object must not be null!");
        }


        UserResponse loggedInUserResponse = authService.authenticate(credentials);

        logger.info("Establishing user session for user: {}", loggedInUserResponse.getUsername());

        userSession = req.getSession();
        
        userSession.setAttribute("loggedInUser", loggedInUserResponse);

        return loggedInUserResponse;

    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest req) {
        req.getSession().invalidate();
    }

}
