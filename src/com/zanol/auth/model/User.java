package com.zanol.auth.model;

import java.util.Objects;

public class User {
    private String code;
    private String password;

    public User(String code, String password) {
        this.code = code;
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean incompleteData() {
        return Objects.isNull(this.code)
                || Objects.isNull(this.password)
                || this.code.isBlank()
                || this.password.isBlank();
    }

    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }
}