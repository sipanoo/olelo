package com.pro.st.Model;

public class User {
    private String id;
    private String username;
    private String city;
    private String email;
    private String imageURL;

    public User(String username, String imageURL) {

        this.username = username;
        this.imageURL = imageURL;


    }
    public User( String imageURL) {


        this.imageURL = imageURL;


    }
    public User(String id, String username, String city, String email, String imageURL) {
        this.username = username;
        this.city = city;
        this.email = email;
        this.id = id;
        this.imageURL = imageURL;
    }

    public User(String id, String username, String city, String imageURL) {

        this.username = username;
        this.city = city;
        this.id = id;
        this.imageURL = imageURL;

    }


    public String getUserId() {
        return id;
    }

    public void setid(String userId) {
        this.id = userId;
    }

    public String getName() {
        return username;
    }

    public void setuserame(String username) {
        this.username = username;
    }

    public String getcity() {
        return city;
    }

    public void setcity(String phoneNr) {
        this.city = phoneNr;
    }

    public String getEmail() {
        return email;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
