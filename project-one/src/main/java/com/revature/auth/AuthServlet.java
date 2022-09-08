package com.revature.auth;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.common.exceptions.AuthenticationException;
import com.revature.common.exceptions.InvalidRequestException;
import com.revature.users.User;



public class AuthServlet extends HttpServlet {
    
    private final AuthService authService;
    private final ObjectMapper objectMapper;

    public AuthServlet (AuthService authService, ObjectMapper objectMapper){
        this.authService=authService;
        this.objectMapper=objectMapper;
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        

        try{

            Credentials credentials = objectMapper.readValue(req.getInputStream(), Credentials.class);

            User loggedInUser = authService.authenticate(credentials);
            resp.setStatus(200);
            resp.getWriter().write(objectMapper.writeValueAsString(loggedInUser));

        }catch(JsonMappingException e){

            resp.setStatus(400);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 400);
            errorResponse.put("message", "Something wrong with the request body");
            errorResponse.put("timestamp", System.currentTimeMillis());

            resp.getWriter().write(objectMapper.writeValueAsString(errorResponse));

        }catch(InvalidRequestException e){

            resp.setStatus(400);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 400);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());

            resp.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            
        }catch(AuthenticationException e){
            
            resp.setStatus(401); 
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 401);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis()); 

            resp.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }


    }
}
