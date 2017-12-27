package com.example.bas.pset6;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Contains the user information
 */
public class UserClass implements Serializable {
    public String id;
    public String username;
    public HashMap favorites;
    public String email;

    // Default constructor for Firebase
    public UserClass() {}

    public UserClass(String id, String username, HashMap favorites, String email) {
        this.username = username;
        this.favorites = favorites;
        this.email = email;
        this.id = id;
    }
}