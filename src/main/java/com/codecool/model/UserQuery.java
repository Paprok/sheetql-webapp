package com.codecool.model;

public class UserQuery {
    private String userQuery;

    public UserQuery(String userQuery) {
        this.userQuery = userQuery;
    }

    public UserQuery() {
    }

    public void setUserQuery(String userQuery) {
        this.userQuery = userQuery;
    }

    public String getUserQuery() {
        return userQuery;
    }
}
