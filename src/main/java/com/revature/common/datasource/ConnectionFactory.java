package com.revature.common.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.revature.common.exceptions.DataSourceException;

@Component
public class ConnectionFactory {

    Logger logger = LogManager.getFormatterLogger(ConnectionFactory.class);

    @Value("${db-url}")
    private String url;

    @Value("${db-username}")
    private String username;

    @Value("${db-password}")
    private String password;

    private ConnectionFactory() {

        
        
        try {
            Class.forName("org.postgresql.Driver");
        
        } catch (ClassNotFoundException e) {
            logger.fatal("Failed to load PostgreSQL JDBC driver.");
            throw new DataSourceException("Failed to load PostgreSQL JDBC driver.");
        }
    }


    public Connection getConnection() throws SQLException {
        System.out.println("url = " + url);
        System.out.println("username = " + username);
        System.out.println("password = " + password);
        return DriverManager.getConnection(url, username, password);
    }

}

