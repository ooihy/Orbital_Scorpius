package com.example.shanna.orbital2;

public class Users {
    private String FullName;
    private String UserType;
    private String Description;

    public Users(){

    }

    public String getDetails() {
        return FullName + ":  " + Description;
    }

    public void setDetails(String fullName, String description) {
        FullName = fullName;
        Description=description;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName (String fullName) {
        FullName = fullName;
    }


    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }
}
