package com.example.demo.services;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.SignupRequest;

public interface UserServices {

	ResponseEntity<?> signUp(SignupRequest signupRequest);

	ResponseEntity<?> authenticateUser(@Valid LoginRequest loginRequest);

}