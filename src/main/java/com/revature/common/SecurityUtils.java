package com.revature.common;


import javax.servlet.http.HttpSession;

import com.revature.common.exceptions.AuthenticationException;
import com.revature.common.exceptions.AuthorizationException;
import com.revature.users.UserResponse;

public class SecurityUtils {

    private SecurityUtils() {
        super();
    }

    public static void enforceAuthentication(HttpSession userSession) {

        if (userSession == null) {
            throw new AuthenticationException(
                    "Could not find HTTP session on request. Please log in to access this endpoint.");
        }
    }

    public static void enforcePermissions(HttpSession userSession, String expectedRole) {
        if (!((UserResponse) userSession.getAttribute("loggedInUser")).getRoleName().equals(expectedRole)) {
            throw new AuthorizationException();
        }

    }



    public static boolean validateRole(HttpSession userSession, String expectedRole) {

        if (!((UserResponse) userSession.getAttribute("loggedInUser")).getRoleName().equals(expectedRole)) {
            return false;
        }
        return true;
    }

    public static boolean validateUserId(HttpSession userSession, String userId) {

        String id = ((UserResponse) userSession.getAttribute("loggedInUser")).getId();

        if (!userId.equals(id.toString())) {
            return false;
        }
        return true;
    }
    
}