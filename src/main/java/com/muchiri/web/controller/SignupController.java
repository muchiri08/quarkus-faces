package com.muchiri.web.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.constraints.NotBlank;
import com.muchiri.business.users.entity.User;
import com.muchiri.business.users.service.UserService;

import io.quarkus.elytron.security.common.BcryptUtil;

@Named("signup")
@RequestScoped
public class SignupController {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    @Inject
    UserService userService;
    @Inject
    FacesContext facesContext;

    public void user() {
        var user = new User();
        user.name = username;
        user.role = "user";
        user.password = BcryptUtil.bcryptHash(password);

        userService.signup(user);
        facesContext.addMessage(null, new FacesMessage("Signup success. Please login."));
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
