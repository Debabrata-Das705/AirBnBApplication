package com.debabrata.AirBnbApllication.Advice;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

import javax.naming.AuthenticationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException exe){
        APIError error = APIError.builder()
                .timeStamp(LocalDateTime.now())
                .error(exe.getMessage())
                .statusCode(HttpStatus.NOT_FOUND)
                .build();
        return buildErrorResponseEntity(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleAllException(Exception ex){
        APIError error = APIError.builder()
                .timeStamp(LocalDateTime.now())  
                .error(ex.getMessage())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        return  buildErrorResponseEntity(error);
    }

//    @ExceptionHandler(AuthenticationException.class)
//    public ResponseEntity<APIError> sendError(AuthenticationException ex){
//        APIError apiError= new APIError(ex.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
//        return new ResponseEntity<>(apiError,HttpStatus.UNAUTHORIZED);
//    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> handleAllException(AuthenticationException ex){
        APIError error = APIError.builder()
                .timeStamp(LocalDateTime.now())
                .error(ex.getMessage())
                .statusCode(HttpStatus.UNAUTHORIZED)
                .build();
        return  buildErrorResponseEntity(error);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<?>> handleJWTException(JwtException ex){
        APIError error = APIError.builder()
                .timeStamp(LocalDateTime.now())
                .error(ex.getMessage())
                .statusCode(HttpStatus.UNAUTHORIZED)
                .build();
        return  buildErrorResponseEntity(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException ex){
        APIError error = APIError.builder()
                .timeStamp(LocalDateTime.now())
                .error(ex.getMessage())
                .statusCode(HttpStatus.FORBIDDEN)
                .build();
        return  buildErrorResponseEntity(error);
    }



    private ResponseEntity<ApiResponse<?>> buildErrorResponseEntity(APIError apiError){
        return new ResponseEntity<>(new ApiResponse<>(apiError),apiError.getStatusCode());
    }
}
