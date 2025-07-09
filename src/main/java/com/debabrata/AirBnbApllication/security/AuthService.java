package com.debabrata.AirBnbApllication.security;
import com.debabrata.AirBnbApllication.Advice.ResourceNotFoundException;
import com.debabrata.AirBnbApllication.dto.LogInDto;
import com.debabrata.AirBnbApllication.dto.SignUpRequestDto;
import com.debabrata.AirBnbApllication.dto.UserDto;
import com.debabrata.AirBnbApllication.entity.User;
import com.debabrata.AirBnbApllication.enums.Roles;
import com.debabrata.AirBnbApllication.reposittory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private  final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private  final JWTService jwtService;

    public UserDto signUp(SignUpRequestDto signUpRequestDto){

       Optional<User> user = userRepository.findByEmail(signUpRequestDto.getEmail());
        if(user.isPresent()){
            throw new BadCredentialsException("User exist by email "+signUpRequestDto.getEmail());
        }

        User toCreate= modelMapper.map(signUpRequestDto , User.class);
        toCreate.setRoles(Set.of(Roles.GUEST));
        toCreate.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
        User savedUser = userRepository.save(toCreate);
        return  modelMapper.map(savedUser,UserDto.class);

    }

    public String[] logIn(LogInDto logInDto){
      Authentication authentication= authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(logInDto.getEmail(),logInDto.getPassword()));
      User user = (User) authentication.getPrincipal();
      String [] arr = new String[2];
      arr[0]= jwtService.generateAccessToken(user);
      arr[1]=jwtService.generateRefreshToken(user);

      return  arr;
    }

    public String refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        User user=userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        String accessToken = jwtService.generateAccessToken(user);
        return accessToken;

    }
}
