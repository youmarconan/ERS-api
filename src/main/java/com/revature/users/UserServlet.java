package com.revature.users;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    private static Logger logger = LogManager.getLogger(UserServlet.class);

    public UserServlet(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        logger.info("A GET request was received by /project1/users at {}", LocalDateTime.now());

        HttpSession loggedInUserSession = req.getSession(false);

        if (loggedInUserSession == null) {

            resp.setStatus(401);
            resp.getWriter().write(objectMapper.writeValueAsString(new Error(401, "Please log in first!")));

            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(), "Non logged in requester is not permitted to communicate with this endpoint");

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

            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(), "Requester is not permitted to communicate with this endpoint");
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

            logger.info("GET request successfully processed at {}", LocalDateTime.now());

        }catch (InvalidRequestException e) {

            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(), e.getMessage());

            resp.setStatus(400);

            Error error = new Error(400, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));

        }catch (JsonMappingException e) {

            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(), e.getMessage());

            resp.setStatus(400);

            Error error = new Error(400, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));

        }catch (DataSourceException e) {

            logger.error("A data source error occurred at {}, error message: {}", LocalDateTime.now(), e.getMessage());

            resp.setStatus(500);

            Error error = new Error(500, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));
        }catch (ResourceNotFoundException e) {

            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(), e.getMessage());

            resp.setStatus(404);

            Error error = new Error(404, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        logger.info("A POST request was received by /project1/users at {}", LocalDateTime.now());
        
        resp.setContentType("application/json");

        HttpSession loggedInUserSession = req.getSession(false);

        if (loggedInUserSession == null) {

            resp.setStatus(401);
            resp.getWriter().write(objectMapper.writeValueAsString(new Error(401, "Please log in first!")));

            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(), "Non logged in requester is not permitted to communicate with this endpoint");
            
            return;
        }

        UserResponse loggedInUser = (UserResponse) loggedInUserSession.getAttribute("loggedInUser");

        boolean w = loggedInUser.getRoleName().equals("admin");

        if( (!w) ){

            resp.setStatus(403); // FORBIDDEN
            resp.getWriter().write(objectMapper.writeValueAsString(new Error(403, "Requester is not permitted to communicate with this endpoint.")));

            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(), "Non admin requester is not permitted to communicate with this endpoint");
            
            return;

        }

        try {

            NewUserRequest requestBody = objectMapper.readValue(req.getInputStream(), NewUserRequest.class);
            ResponseString generatedId = userService.register(requestBody);
            resp.getWriter().write(objectMapper.writeValueAsString(generatedId));

            logger.info("POST request successfully processed at {}", LocalDateTime.now());

        } catch (InvalidRequestException | JsonMappingException e) {

            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(), e.getMessage());

            resp.setStatus(400);

            Error error = new Error(400, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));

        } catch (IsAlreadyExist e) {

            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(), e.getMessage());

            resp.setStatus(409);

            Error error = new Error(409, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));
        } catch (DataSourceException e) {

            logger.error("A data source error occurred at {}, error message: {}", LocalDateTime.now(), e.getMessage());

            resp.setStatus(500);

            Error error = new Error(500, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));
        }catch (ResourceNotFoundException e) {

            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(), e.getMessage());

            resp.setStatus(404);

            Error error = new Error(404, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        logger.info("A PUT request was received by /project1/users at {}", LocalDateTime.now());

        HttpSession loggedInUserSession = req.getSession(false);

        if (loggedInUserSession == null) {

            resp.setStatus(401);
            resp.getWriter().write(objectMapper.writeValueAsString(new Error(401, "Please log in first!")));

            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(), "Non logged in requester is not permitted to communicate with this endpoint");

            return;
        }

        UserResponse loggedInUser = (UserResponse) loggedInUserSession.getAttribute("loggedInUser");

        boolean w = loggedInUser.getRoleName().equals("admin");

        if( (!w) ){

            resp.setStatus(403); // FORBIDDEN
            resp.getWriter().write(objectMapper.writeValueAsString(new Error(403, "Requester is not permitted to communicate with this endpoint.")));

            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(), "Non admin requester is not permitted to communicate with this endpoint");
            
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

            logger.info("PUT request successfully processed at {}", LocalDateTime.now());

        } catch (InvalidRequestException | JsonMappingException e) {

            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(), e.getMessage());

            resp.setStatus(400);

            Error error = new Error(400, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));

        } catch (DataSourceException e) {

            logger.error("A data source error occurred at {}, error message: {}", LocalDateTime.now(), e.getMessage());

            resp.setStatus(500);

            Error error = new Error(500, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));
        } catch(NullPointerException e){
            
            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(), e.getMessage());

            resp.setStatus(400);

            Error error = new Error(400, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));

        }catch (ResourceNotFoundException e) {

            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(), e.getMessage());

            resp.setStatus(404);

            Error error = new Error(404, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));
        }

    }

}
