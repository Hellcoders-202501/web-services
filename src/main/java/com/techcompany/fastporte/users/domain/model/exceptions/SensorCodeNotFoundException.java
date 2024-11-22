package com.techcompany.fastporte.users.domain.model.exceptions;

public class SensorCodeNotFoundException extends RuntimeException {
    public SensorCodeNotFoundException(String code) {
        super("Sensor code not found: " + code);
    }
}
