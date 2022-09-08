package com.revature.common.exceptions;

public class DataSourceException extends RuntimeException {
    
    public DataSourceException(Throwable cause) {
        super("Something went wrong when communicating with the database", cause);
    }
    
}
