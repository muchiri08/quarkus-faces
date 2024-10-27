package com.muchiri.web.controller;

import java.util.List;

import com.muchiri.business.users.entity.User;
import com.muchiri.business.users.service.UserService;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("home")
@RequestScoped
public class HomeController {

    @Inject
    SecurityIdentity identity;
    @Inject
    UserService userService;

    List<User> users;

    @PostConstruct
    public void init() {
        this.users = userService.allUsers();
    }

    public String getUsername() {
        return identity.getPrincipal().getName();
    }

    public UserService getUserService() {
        return userService;
    }

    public List<User> getUsers() {
        return users;
    }

}
