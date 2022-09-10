package com.revature.common;

public class ResponseString {

    private String message;

    public ResponseString(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResponseString [message=" + message + "]";
    }
    

}
