package com.techcompany.fastporte.shared.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class SuccessResponse {
    private String message;
    private String timestamp;

    public SuccessResponse(String message) {
        this.message = message;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.timestamp = LocalDateTime.now().withNano(0).format(formatter);

    }

}
