package com.revature;

import com.revature.users.UserDAO;


public class App {
    public static void main(String[] args) {

        UserDAO userDAO = new UserDAO();

        System.out.println(userDAO.login("youmarco", "p@$$word"));
    }

}
