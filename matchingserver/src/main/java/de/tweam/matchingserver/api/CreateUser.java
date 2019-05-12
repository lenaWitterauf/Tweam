package de.tweam.matchingserver.api;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<String> getKeywordsLowercase() {
        return keywords.stream().map(String::toLowerCase).collect(Collectors.toList());
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
}
