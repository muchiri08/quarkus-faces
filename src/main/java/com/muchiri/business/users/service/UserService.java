package com.muchiri.business.users.service;

import java.util.List;

import com.muchiri.business.users.entity.User;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserService {
    @Inject
    private EntityManager em;

    public List<User> allUsers() {
        return em.createNamedQuery("AllUsers", User.class).getResultList();
    }

    @Transactional
    public void signup(User user) {
        em.persist(user);
    }
}
