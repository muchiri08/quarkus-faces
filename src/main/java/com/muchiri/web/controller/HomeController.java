package com.muchiri.web.controller;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("home")
@RequestScoped
public class HomeController {

    @Inject
    SecurityIdentity identity;

    public String getUsername() {
        return identity.getPrincipal().getName();
    }
}
