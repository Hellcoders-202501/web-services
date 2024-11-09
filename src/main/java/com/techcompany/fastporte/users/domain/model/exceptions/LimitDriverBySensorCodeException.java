package com.techcompany.fastporte.users.domain.model.exceptions;

public class LimitDriverBySensorCodeException extends RuntimeException {
    public LimitDriverBySensorCodeException(String code) {
        super("The sensor code " + code + " has reached the limit of drivers");
    }
}
