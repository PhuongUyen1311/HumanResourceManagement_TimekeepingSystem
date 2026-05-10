package com.qlns.service;

import com.qlns.dao.AuthDAO;

public class AuthService {
    private AuthDAO authDAO;

    public AuthService() {
        authDAO = new AuthDAO();
    }

    public boolean login(String username, String password) {
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            return false;
        }
        return authDAO.login(username, password);
    }
}
