package com.techcompany.fastporte.shared.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class ErrorResponse {
    private String error;
    private String stackTrace;
    private String timestamp;

    public ErrorResponse(Exception exception) {
        this.error = exception.getMessage();
        this.stackTrace = exception.getStackTrace()[0].toString();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.timestamp = LocalDateTime.now().withNano(0).format(formatter);

    }

    public ErrorResponse(String error) {
        this.error = error;
        this.stackTrace = "";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.timestamp = LocalDateTime.now().withNano(0).format(formatter);
    }
}
