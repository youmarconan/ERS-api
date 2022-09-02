package com.revature;

import java.sql.SQLException;

import com.revature.common.datasource.ConnectionFactory;
import com.revature.users.Admin;
import com.revature.users.Employee;
import com.revature.users.Manager;

public class App 
{
    public static void main( String[] args )
    {
        try {
            ConnectionFactory.getInstance().getConnection();
            System.out.println( "Successful Connection!" );
        } catch (SQLException e) {
        
            e.printStackTrace();
        }

        Admin admin = new Admin();
        System.out.println(admin);

        Manager manager = new Manager();
        System.out.println(manager);

        Employee employee = new Employee();
        System.out.println(employee);

    }
}
