package com.debabrata.AirBnbApllication.Advice;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ApiResponse<T> {

    @JsonFormat(pattern = "dd-MM-yyyy :: HH:mm:ss")
    private LocalDateTime timeStamp;

    private T data;

    private APIError error;

    public ApiResponse(){
        this.timeStamp=LocalDateTime.now();
    }

     public ApiResponse(T data){
        this();
        this.data=data;
    }
     public ApiResponse(APIError error ){
        this();
        this.error=error;
    }


}
