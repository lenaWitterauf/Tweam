package de.tweam.matchingserver.api;

import java.util.List;

public class CreateUser {
    String handle;
    List<String> keywords;

    public CreateUser(){

    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
}
