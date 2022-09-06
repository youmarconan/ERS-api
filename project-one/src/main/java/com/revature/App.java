package com.revature;

import java.sql.SQLException;

import com.revature.common.datasource.ConnectionFactory;
import com.revature.users.AdminDAO;
import com.revature.users.EmployeeDAO;
import com.revature.users.ManagerDAO;

public class App {
    public static void main(String[] args) {
        try {
            ConnectionFactory.getInstance().getConnection();
            System.out.println("Successful Connection!");
        } catch (SQLException e) {

            e.printStackTrace();
        }

        AdminDAO admin = new AdminDAO();
        System.out.println(admin);

        ManagerDAO manager = new ManagerDAO();
        System.out.println(manager);

        EmployeeDAO employee = new EmployeeDAO();
        System.out.println(employee);

    }

}
