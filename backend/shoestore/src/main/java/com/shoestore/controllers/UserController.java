package com.shoestore.controllers;

import com.shoestore.dtos.UserDTO;
import com.shoestore.dtos.UserLoginDTO;
import com.shoestore.exceptions.DataNotFoundException;
import com.shoestore.models.User;
import com.shoestore.response.LoginResponse;
import com.shoestore.services.UserService;
import com.shoestore.components.LocalizationUtils;
import com.shoestore.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final LocalizationUtils localizationUtils;
    @PostMapping("/register")
    // Can we register an "admin" user?
    public ResponseEntity<?> createUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result
            ) {
        try{
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                return ResponseEntity.badRequest().body(localizationUtils.getLocalizedMessaged(MessageKeys.PASSWORD_NOT_MATCH));
            }
            User user = userService.createUser(userDTO);
            return ResponseEntity.ok(user);
        }  catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO
            ) {
        // Check User Login and return token
        try {
            String token  = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
            // Return token
            // Convert to JSON format
            return ResponseEntity.ok(LoginResponse.builder()
                            .message(localizationUtils.getLocalizedMessaged(MessageKeys.LOGIN_SUCCESSFULLY))
                            .token(token)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    LoginResponse.builder()
                            .message(localizationUtils.getLocalizedMessaged(MessageKeys.LOGIN_FAILED, e.getMessage()))
                            .build()
            );
        }
    }
}
