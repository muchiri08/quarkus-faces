package com.muchiri.web.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.muchiri.web.InvalidLoginCredentialsException;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;

/**
 *
 * @author muchiri
 */
@Named("auth")
@RequestScoped
public class AuthController {

    @NotBlank
    private String username;
    @NotBlank
    private String password;

    @ConfigProperty(name = "quarkus.http.auth.form.cookie-name")
    String cookieName;
    @Inject
    FacesContext facesContext;

    public String logout() {
        var fcResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        var cookie = new Cookie(cookieName, "");
        cookie.setMaxAge(0);
        fcResponse.addCookie(cookie);
        return "/index.xhtml?faces-redirect=true";
    }

    public void authenticate() {
        System.out.println("authenticating");
        try {
            var request = (HttpServletRequest) facesContext.getExternalContext().getRequest();

            generateCookie(request);

            // redirect to your main page.
            facesContext.getExternalContext().redirect("/page.xhtml");
        } catch (InvalidLoginCredentialsException e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        } catch (Exception ex) {
            // do something?
        }
    }

    private void generateCookie(HttpServletRequest request) throws IOException, InterruptedException {
        System.out.println("Generate Cookie called");
        var securityCheckUrl = request.getRequestURL().toString()
                .replace("/index.xhtml", "/j_security_check");
        var response = jSecurityCheckRequest(securityCheckUrl);
        System.out.println(" STATUS: " + response.statusCode());
        if (response.statusCode() == 401) {
            throw new InvalidLoginCredentialsException("invalid user credentials");
        }

        var fcResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        setCookie(response, fcResponse);
    }

    private void setCookie(HttpResponse<String> response, HttpServletResponse fcResponse) {
        var responseMap = response.headers().map();
        if (responseMap.containsKey("set-cookie")) {
            var cookieString = responseMap.get("set-cookie").get(0);
            var quarkusCookie = new Cookie(cookieName, cookieString.split("=")[1]);
            quarkusCookie.setMaxAge(8 * 60 * 60);
            quarkusCookie.setHttpOnly(true);
            fcResponse.addCookie(quarkusCookie);
        }
    }

    private HttpResponse<String> jSecurityCheckRequest(String securityCheckUrl)
            throws IOException, InterruptedException {
        var response = HttpClient.newHttpClient().send(HttpRequest.newBuilder()
                .uri(URI.create(securityCheckUrl))
                .POST(HttpRequest.BodyPublishers.ofString(
                        "j_username=" + username + "&j_password=" + password))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build(), HttpResponse.BodyHandlers.ofString());
        return response;
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
