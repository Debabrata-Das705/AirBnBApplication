package com.debabrata.AirBnbApllication.Advice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor

public class APIError {

    @JsonFormat(pattern = "dd-MM-yyyy :: HH:mm:ss")
    private LocalDateTime timeStamp;

    private String error;
    private HttpStatus statusCode;

    public APIError() {
        this.timeStamp = LocalDateTime.now();
    }

    public APIError(String error, HttpStatus statusCode) {
        this();
        this.error = error;
        this.statusCode = statusCode;
    }
}
