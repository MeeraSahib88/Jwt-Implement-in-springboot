package com.example.demo.dto;

import java.util.Set;

import lombok.Data;
@Data
public class SignupRequest {
	
	private String email;

	private Set<String> role;

	private String password;

	private String username;


}
