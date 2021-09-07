package com.zanol.auth.engine;

import com.zanol.auth.exception.AuthenticationException;
import com.zanol.auth.model.User;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AuthEngine {

    private static final List<User> REGISTERED_USERS = List.of(
            new User("INSERTCODE", "INSERTPASSWORD")
    );

    public static void authenticate(User user) {
        if (Objects.isNull(user) || user.incompleteData())
            throw new AuthenticationException("Please enter user data!");

        getUserByCredentials(user.getCode(), user.getPassword())
                .orElseThrow(() -> new AuthenticationException("Invalid username and/or password"));
    }

    private static Optional<User> getUserByCredentials(String code, String pass) {
        return REGISTERED_USERS
                .stream()
                .filter(registeredUser -> registeredUser.getCode().equals(code) && registeredUser.validatePassword(pass))
                .findFirst();
    }
}