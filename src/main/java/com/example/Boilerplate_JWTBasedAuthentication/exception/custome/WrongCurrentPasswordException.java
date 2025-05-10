package com.example.Boilerplate_JWTBasedAuthentication.exception.custome;

public class WrongCurrentPasswordException extends Exception{
    public WrongCurrentPasswordException(String message) {
        super(message);
    }
}
