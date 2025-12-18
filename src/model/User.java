package model;

public class User {
    private String username;
    private String passwordHash;

    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public String getPasswordHash() {return passwordHash;}
    public String getUsername() {return username;}

    // no need for setters, as update feature is not available yet

}
