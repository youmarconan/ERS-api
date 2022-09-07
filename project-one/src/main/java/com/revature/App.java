package com.revature;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import com.revature.users.UserDAO;
import com.revature.users.UserServlet;


public class App {
    public static void main(String[] args) throws LifecycleException {

        // UserDAO userDAO = new UserDAO();
        // System.out.println(userDAO.login("youmarco", "p@$$word"));

        String docBase = System.getProperty("java.io.tmpdir");
        Tomcat webServer =new Tomcat();
        webServer.setBaseDir(docBase);
        webServer.setPort(5000);
        webServer.getConnector();

        webServer.addContext("/project1", docBase);
        webServer.addServlet("/project1", "UserServlet", new UserServlet()).addMapping("/users");

        webServer.start();
        webServer.getServer().await();

        System.out.println("Web app successflly started!");
    }

}
