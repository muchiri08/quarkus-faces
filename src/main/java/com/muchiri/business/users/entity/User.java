package com.muchiri.business.users.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@NamedQuery(name = "AllUsers", query = "SELECT u FROM User u")
@Table(name = "users")
public class User {
    @Id
    public Integer id;
    public String username;
    public String role;
}
