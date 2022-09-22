package com.revature;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.revature.auth.AuthService;
import com.revature.auth.AuthServlet;
import com.revature.reimbursements.ReimbursementDAO;
import com.revature.reimbursements.ReimbursementService;
import com.revature.reimbursements.ReimbursementServlet;
import com.revature.reimbursements.UpdateOwnReimbursementServlet;
import com.revature.users.UserDAO;
import com.revature.users.UserService;
import com.revature.users.UserServlet;


public class App {
    private static Logger logger = LogManager.getFormatterLogger(App.class);
    public static void main(String[] args) throws LifecycleException {

        logger.info("Starting Project One");

        String docBase = System.getProperty("java.io.tmpdir");
        Tomcat webServer =new Tomcat();
        webServer.setBaseDir(docBase);
        webServer.setPort(5000);
        webServer.getConnector();

        UserDAO userDAO = new UserDAO();
        ReimbursementDAO reimbursementDAO = new ReimbursementDAO();

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

        AuthService authService =new AuthService(userDAO);
        UserService userService =new UserService(userDAO);
        ReimbursementService reimbursementService = new ReimbursementService(reimbursementDAO);

        UserServlet userServlet = new UserServlet(userService, objectMapper);
        AuthServlet authServlet = new AuthServlet(authService, objectMapper);
        ReimbursementServlet reimbursementServlet = new ReimbursementServlet(reimbursementService, objectMapper);
        UpdateOwnReimbursementServlet updateOwnReimbursementServlet = new UpdateOwnReimbursementServlet(reimbursementService, objectMapper);

        String rootContext = "/project1";

        webServer.addContext(rootContext, docBase);

        webServer.addServlet(rootContext, "UserServlet", userServlet).addMapping("/users");
        
        webServer.addServlet(rootContext, "AuthServlet", authServlet).addMapping("/auth");

        webServer.addServlet(rootContext, "ReimbursementServlet", reimbursementServlet).addMapping("/reimbursement");

        webServer.addServlet(rootContext, "UpdateOwnReimbursementServlet", updateOwnReimbursementServlet).addMapping("/updateOwnReimbursement");

        webServer.start();
        logger.info("Web application successfully started");
        webServer.getServer().await();

    
     
    }

}
