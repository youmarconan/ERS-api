package com.revature.users;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserServlet extends HttpServlet {
    
    private final UserDAO userDAO;
    private final ObjectMapper objectMapper;

    public UserServlet (UserDAO userDAO, ObjectMapper objectMapper){
        this.userDAO=userDAO;
        this.objectMapper=objectMapper;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        

        resp.setContentType("application/json");

        resp.getWriter().write(objectMapper.writeValueAsString(userDAO.allUsers()));
    }
}
