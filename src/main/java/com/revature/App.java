package com.revature;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


import com.revature.auth.AuthServlet;

import com.revature.config.AppConfig;

import com.revature.reimbursements.ReimbursementServlet;
import com.revature.reimbursements.UpdateOwnReimbursementServlet;

import com.revature.users.UserServlet;

public class App {
    private static Logger logger = LogManager.getFormatterLogger(App.class);

    public static void main(String[] args) throws LifecycleException {

        logger.info("Starting Project Two");

        try (AnnotationConfigApplicationContext beanContainer = new AnnotationConfigApplicationContext(
                AppConfig.class)) {

            String docBase = System.getProperty("java.io.tmpdir");
            Tomcat webServer = new Tomcat();
            webServer.setBaseDir(docBase);
            webServer.setPort(5000);
            webServer.getConnector();
            
            // ObjectMapper objectMapper = new ObjectMapper();
            // objectMapper.registerModule(new JavaTimeModule());

            UserServlet userServlet = beanContainer.getBean(UserServlet.class);
            AuthServlet authServlet = beanContainer.getBean(AuthServlet.class);
            ReimbursementServlet reimbursementServlet = beanContainer.getBean(ReimbursementServlet.class);

            UpdateOwnReimbursementServlet updateOwnReimbursementServlet = beanContainer.getBean(UpdateOwnReimbursementServlet.class);

            String rootContext = "/project2";

            webServer.addContext(rootContext, docBase);

            webServer.addServlet(rootContext, "UserServlet", userServlet).addMapping("/users");

            webServer.addServlet(rootContext, "AuthServlet", authServlet).addMapping("/auth");

            webServer.addServlet(rootContext, "ReimbursementServlet", reimbursementServlet)
                    .addMapping("/reimbursement");

            /*webServer.addServlet(rootContext, "UpdateOwnReimbursementServlet", updateOwnReimbursementServlet)
                    .addMapping("/updateOwnReimbursement");*/

            webServer.start();
            logger.info("Web application successfully started");
            webServer.getServer().await();

        }

    }

}
