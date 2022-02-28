package com.example.demo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.SignupRequest;
import com.example.demo.services.UserServices;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class UserController {
	
	@Autowired
	UserServices userservices;
	
	@PostMapping("/sign")
	public ResponseEntity<?> signUp(@RequestBody SignupRequest signupRequest){
		return userservices.signUp(signupRequest);
	}
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		return userservices.authenticateUser(loginRequest);
		
	}
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
	public String userAccess() {
		return "User Content.";
	}
	@GetMapping("/mod")
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public String moderatorAccess() {
		return "Moderator Board.";
	}
	@GetMapping("/admin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}
}
