package com.revature;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.revature.auth.AuthService;
import com.revature.auth.AuthServlet;
import com.revature.reimbursements.ReimbursementDAO;
import com.revature.users.UserDAO;
import com.revature.users.UserService;
import com.revature.users.UserServlet;


public class App {
    public static void main(String[] args) throws LifecycleException {

        // String docBase = System.getProperty("java.io.tmpdir");
        // Tomcat webServer =new Tomcat();
        // webServer.setBaseDir(docBase);
        // webServer.setPort(5000);
        // webServer.getConnector();

        // UserDAO userDAO = new UserDAO();
        // ObjectMapper objectMapper = new ObjectMapper();
        // objectMapper.registerModule(new JavaTimeModule());

        // AuthService authService =new AuthService(userDAO);
        // UserService userService =new UserService(userDAO);

        // UserServlet userServlet = new UserServlet(userService, objectMapper);
        // AuthServlet authServlet = new AuthServlet(authService, objectMapper);

        // String rootContext = "/project1";

        // webServer.addContext(rootContext, docBase);

        // webServer.addServlet(rootContext, "UserServlet", userServlet).addMapping("/users");
        
        // webServer.addServlet(rootContext, "AuthServlet", authServlet).addMapping("/auth");

        // webServer.start();
        // webServer.getServer().await();

       ReimbursementDAO reimbursementDAO = new ReimbursementDAO();
       System.out.println(reimbursementDAO.findReimbursementById("2"));
       System.out.println(reimbursementDAO.approveOrDenyReimbursement("1", "2", "5"));
       System.out.println(reimbursementDAO.findReimbursementById("2"));
    }

}
