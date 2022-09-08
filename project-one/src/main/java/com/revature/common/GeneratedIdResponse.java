package com.revature.common;

public class GeneratedIdResponse {
    
    private String generatedId;

    public GeneratedIdResponse(String generatedId) {
        this.generatedId = generatedId;
    }

    public String getGeneratedId() {
        return generatedId;
    }

    public void setGeneratedId(String generatedId) {
        this.generatedId = generatedId;
    }

    @Override
    public String toString() {
        return "GeneratedIdResponse [generatedId=" + generatedId + "]";
    }


}
