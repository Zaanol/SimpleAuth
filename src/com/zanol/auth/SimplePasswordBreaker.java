package com.zanol.auth;

import com.zanol.auth.engine.BruteForceEngine;
import com.zanol.auth.exception.AuthenticationException;

public class SimplePasswordBreaker {

    public static void main(String[] args) {
        validadeArgsAndPerformAction(args);
    }

    public static void validadeArgsAndPerformAction(String[] args) {
        if (args.length >= 3) {
            BruteForceEngine.bruteForce(args[0], args[1], args[2]);
        } else {
            throw new AuthenticationException("Incorrent amount of arguments");
        }
    }
}