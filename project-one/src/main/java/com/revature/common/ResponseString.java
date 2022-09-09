package com.revature.common;

public class ResponseString {
    
    private String response;

    public ResponseString(String response) {
        this.response = response;
    }

    public String getresponse() {
        return response;
    }

    public void setresponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "ResponseString [response=" + response + "]";
    }


}
