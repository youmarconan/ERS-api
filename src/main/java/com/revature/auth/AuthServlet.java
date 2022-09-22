package com.revature.auth;
import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.common.Error;
import com.revature.common.exceptions.AuthenticationException;
import com.revature.common.exceptions.DataSourceException;
import com.revature.common.exceptions.InvalidRequestException;
import com.revature.users.UserResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;


@Controller
public class AuthServlet extends HttpServlet {

    
    
    private final AuthService authService;
    private final ObjectMapper objectMapper;
    
    private static Logger logger = LogManager.getLogger(AuthServlet.class);

    public AuthServlet (AuthService authService, ObjectMapper objectMapper){
        this.authService=authService;
        this.objectMapper=objectMapper;
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        logger.info("A POST request was received by /project1/auth at {}", LocalDateTime.now());

        try{

            Credentials credentials = objectMapper.readValue(req.getInputStream(), Credentials.class);
            UserResponse loggedInUserResponse = authService.authenticate(credentials);

            HttpSession userSession = req.getSession();
            userSession.setAttribute("loggedInUser", loggedInUserResponse);
            
            
            resp.setStatus(200);
            resp.getWriter().write(objectMapper.writeValueAsString(loggedInUserResponse));

            logger.info("POST request successfully processed at {}", LocalDateTime.now());

        }catch(JsonMappingException e){

            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(), e.getMessage());

            resp.setStatus(400);

            Error error = new Error(400, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));

        }catch(InvalidRequestException e){

            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(), e.getMessage());

            resp.setStatus(400);

            Error error = new Error(400, e.getMessage());
            

            resp.getWriter().write(objectMapper.writeValueAsString(error));
            
        }catch(AuthenticationException e){

            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(), e.getMessage());
            
            resp.setStatus(401); 

            Error error = new Error(401, e.getMessage());
            

            resp.getWriter().write(objectMapper.writeValueAsString(error));

        }catch(DataSourceException e){

            logger.error("A data source error occurred at {}, error message: {}", LocalDateTime.now(), e.getMessage());

            resp.setStatus(500); 

            Error error = new Error(500, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));
        }


    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        logger.info("A DELETE request was received by /project1/auth at {}", LocalDateTime.now());

        req.getSession().invalidate(); // this effectively "logs out" the requester by invalidating the session within the server

    }
}
