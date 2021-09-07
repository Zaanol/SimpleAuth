package com.zanol.auth;

import com.zanol.auth.engine.AuthEngine;
import com.zanol.auth.exception.AuthenticationException;
import com.zanol.auth.model.AuthResult;
import com.zanol.auth.model.User;

import java.util.Objects;

public class SipleAuth {
    private static final AuthResult INTERNAL_ERROR =
            new AuthResult(1, "Internal Error");

    private static final AuthResult AUTH_SUCCESS =
            new AuthResult(0, "Access granted");

    public static void main(String[] args) {
        AuthResult result = null;

        try {
            User user = argsToUser(args);

            AuthEngine.authenticate(user);

            result = AUTH_SUCCESS;
        } catch (AuthenticationException e) {
            result = new AuthResult(INTERNAL_ERROR.getCode(), e.getMessage());
        } catch (Exception e) {
            result = INTERNAL_ERROR;
        } finally {
            if (Objects.isNull(result))
                result = INTERNAL_ERROR;
        }

        System.out.println(result.getMessage());
        System.exit(result.getCode());
    }


    public static User argsToUser(String[] args) {
        if (args.length >= 2) {
            return new User(args[0], args[1]);
        } else {
            throw new AuthenticationException("Incorrent amount of arguments");
        }
    }
}