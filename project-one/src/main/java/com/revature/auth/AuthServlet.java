package com.revature.auth;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.users.UserDAO;



public class AuthServlet extends HttpServlet {
    
    private final UserDAO userDAO;
    private final ObjectMapper objectMapper;

    public AuthServlet (UserDAO userDAO, ObjectMapper objectMapper){
        this.userDAO=userDAO;
        this.objectMapper=objectMapper;
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        Credentials credentials = objectMapper.readValue(req.getInputStream(), Credentials.class);

        com.revature.users.User loggedInUser = userDAO.login(credentials.getUsername(), credentials.getPassword()).orElseThrow(()->new RuntimeException("No user found with those credentials"));


        resp.getWriter().write(objectMapper.writeValueAsString(loggedInUser));


    }
}
