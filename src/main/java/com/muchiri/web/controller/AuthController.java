package com.muchiri.web.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

/**
 *
 * @author muchiri
 */
@Named("auth")
@RequestScoped
public class AuthController {

    private String username;
    private String password;

    public void authenticate() {
        System.out.println("authenticating");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
