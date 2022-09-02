package com.revature;

import java.sql.SQLException;

import com.revature.common.datasource.ConnectionFactory;

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
    }
}
