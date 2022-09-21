package com.revature.common.exceptions;

public class IsAlreadyExist extends RuntimeException {
    
    public IsAlreadyExist (String message){
        super(message);
    }
}
