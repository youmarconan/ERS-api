package com.revature.common;

public class GeneratedResponse {

    private String generatedString;

    public GeneratedResponse() {
    }

    public GeneratedResponse(String generatedString) {
        this.generatedString = generatedString;
    }

    public String getGeneratedString() {
        return generatedString;
    }

    public void setGeneratedString(String generatedString) {
        this.generatedString = generatedString;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((generatedString == null) ? 0 : generatedString.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GeneratedResponse other = (GeneratedResponse) obj;
        if (generatedString == null) {
            if (other.generatedString != null)
                return false;
        } else if (!generatedString.equals(other.generatedString))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "GeneratedResponse [generatedString=" + generatedString + "]";
    }

    
}
