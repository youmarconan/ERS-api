package com.revature.users;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.common.ResponseString;
import com.revature.common.Error;
import com.revature.common.exceptions.DataSourceException;
import com.revature.common.exceptions.InvalidRequestException;
import com.revature.common.exceptions.IsAlreadyExist;
import com.revature.common.exceptions.ResourceNotFoundException;

public class UserServlet extends HttpServlet {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    public UserServlet(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession loggedInUserSession = req.getSession(false);

        if (loggedInUserSession == null) {
            resp.setStatus(401);
            resp.getWriter().write(objectMapper.writeValueAsString(new Error(401, "Please log in first!")));
            return;
        }

        UserResponse loggedInUser = (UserResponse) loggedInUserSession.getAttribute("loggedInUser");

        resp.setContentType("application/json");

        String id = req.getParameter("id");
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        
        boolean w = loggedInUser.getRoleName().equals("admin");
        boolean x = loggedInUser.getId().equals(id);

        if( (!w && !x) ){

            resp.setStatus(403); // FORBIDDEN
            resp.getWriter().write(objectMapper.writeValueAsString(new Error(403, "Requester is not permitted to communicate with this endpoint.")));
            return;

        }

        try{
            if(id == null && username == null && email== null){
                List<UserResponse> allUsers = userService.getAllUsers();
                resp.getWriter().write(objectMapper.writeValueAsString(allUsers));
            }
            if(id != null)
            {
                UserResponse userResponse = userService.getUserById(id);
                resp.getWriter().write(objectMapper.writeValueAsString(userResponse));
            }
            if(username != null)
            {
                UserResponse userResponse = userService.getUserByUsername(username);
                resp.getWriter().write(objectMapper.writeValueAsString(userResponse));
            }
            if(email != null)
            {
                UserResponse userResponse = userService.getUserByEmail(email);
                resp.getWriter().write(objectMapper.writeValueAsString(userResponse));
            }

        }catch (InvalidRequestException e) {

            resp.setStatus(400);

            Error error = new Error(400, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));

        }catch (JsonMappingException e) {

            resp.setStatus(400);

            Error error = new Error(400, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));

        }catch (DataSourceException e) {

            resp.setStatus(500);

            Error error = new Error(500, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));
        }catch (ResourceNotFoundException e) {

            resp.setStatus(400);

            Error error = new Error(400, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        HttpSession loggedInUserSession = req.getSession(false);

        if (loggedInUserSession == null) {
            resp.setStatus(401);
            resp.getWriter().write(objectMapper.writeValueAsString(new Error(401, "Please log in first!")));
            return;
        }

        UserResponse loggedInUser = (UserResponse) loggedInUserSession.getAttribute("loggedInUser");

        boolean w = loggedInUser.getRoleName().equals("admin");

        if( (!w) ){

            resp.setStatus(403); // FORBIDDEN
            resp.getWriter().write(objectMapper.writeValueAsString(new Error(403, "Requester is not permitted to communicate with this endpoint.")));
            return;

        }

        try {

            NewUserRequest requestBody = objectMapper.readValue(req.getInputStream(), NewUserRequest.class);
            ResponseString generatedId = userService.register(requestBody);
            resp.getWriter().write(objectMapper.writeValueAsString(generatedId));

        } catch (InvalidRequestException | JsonMappingException e) {

            resp.setStatus(400);

            Error error = new Error(400, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));

        } catch (IsAlreadyExist e) {

            resp.setStatus(409);

            Error error = new Error(409, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));
        } catch (DataSourceException e) {

            resp.setStatus(500);

            Error error = new Error(500, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession loggedInUserSession = req.getSession(false);

        if (loggedInUserSession == null) {
            resp.setStatus(401);
            resp.getWriter().write(objectMapper.writeValueAsString(new Error(401, "Please log in first!")));
            return;
        }

        UserResponse loggedInUser = (UserResponse) loggedInUserSession.getAttribute("loggedInUser");

        boolean w = loggedInUser.getRoleName().equals("admin");

        if( (!w) ){

            resp.setStatus(403); // FORBIDDEN
            resp.getWriter().write(objectMapper.writeValueAsString(new Error(403, "Requester is not permitted to communicate with this endpoint.")));
            return;

        }

        resp.setContentType("application/json");

        

        try {
            String toBeUpdated = req.getParameter("update");
            UpdateRequestBody requestBody = objectMapper.readValue(req.getInputStream(), UpdateRequestBody.class);

            if (toBeUpdated == null) {

                throw new InvalidRequestException("Provided request must not be null!");
                
            }

            if (toBeUpdated.equals("firstname")) {
                ResponseString generatedId = userService.updateFristNmae(requestBody);
                resp.getWriter().write(objectMapper.writeValueAsString(generatedId));
            }

            if (toBeUpdated.equals("lastname")) {
                ResponseString generatedId = userService.updateLastNmae(requestBody);
                resp.getWriter().write(objectMapper.writeValueAsString(generatedId));
            }

            if (toBeUpdated.equals("email")) {
                ResponseString generatedId = userService.updateEmail(requestBody);
                resp.getWriter().write(objectMapper.writeValueAsString(generatedId));
            }

            if (toBeUpdated.equals("password")) {
                ResponseString generatedId = userService.updatePassword(requestBody);
                resp.getWriter().write(objectMapper.writeValueAsString(generatedId));
            }

            if (toBeUpdated.equals("isactive")) {
                ResponseString generatedId = userService.updateIsActive(requestBody);
                resp.getWriter().write(objectMapper.writeValueAsString(generatedId));
            }

            if (toBeUpdated.equals("roleid")) {
                ResponseString generatedId = userService.updateRoleId(requestBody);
                resp.getWriter().write(objectMapper.writeValueAsString(generatedId));
            }

        } catch (InvalidRequestException | JsonMappingException e) {

            resp.setStatus(400);

            Error error = new Error(400, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));

        } catch (DataSourceException e) {

            resp.setStatus(500);

            Error error = new Error(500, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));
        } catch(NullPointerException e){

            resp.setStatus(400);

            Error error = new Error(400, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));

        }

    }

}
