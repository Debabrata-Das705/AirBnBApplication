package com.debabrata.AirBnbApllication.controller;

import com.debabrata.AirBnbApllication.dto.LogInDto;
import com.debabrata.AirBnbApllication.dto.LoginResponseDTO;
import com.debabrata.AirBnbApllication.dto.SignUpRequestDto;
import com.debabrata.AirBnbApllication.dto.UserDto;
import com.debabrata.AirBnbApllication.security.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignUpRequestDto signUpRequestDto){
        return new ResponseEntity<>(authService.signUp(signUpRequestDto), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LogInDto logInDto, HttpServletRequest request, HttpServletResponse response){
       String [] tokens=authService.logIn(logInDto);
        Cookie cookie=new Cookie("refreshToken",tokens[1]);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return  ResponseEntity.ok(new LoginResponseDTO(tokens[0]));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(HttpServletRequest request){
        String refreshToken= Arrays.stream(request.getCookies())
                .filter(cookie ->"refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(cookie ->cookie.getValue())
                .orElseThrow(() ->new AuthenticationServiceException("Refresh token not found"));
        String token=authService.refreshToken(refreshToken);
        return new ResponseEntity<>(new LoginResponseDTO(token), HttpStatus.OK);
    }

}
