package com.example.demo.servicesImp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.config.JwtUtils;
import com.example.demo.dto.JwtResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.MessageResponse;
import com.example.demo.dto.SignupRequest;
import com.example.demo.entity.ERole;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.UserServices;
@Service
public class UserServiesImpl implements UserServices{
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired 
	PasswordEncoder encoder;
	
	@Autowired
	Environment environment;
	
	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	AuthenticationManager authenticationManager;
	

	@Override
	public ResponseEntity<?> signUp(SignupRequest signupRequest) {
		if (userRepository.existsByUsername(signupRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse(HttpStatus.PARTIAL_CONTENT.value(),environment.getProperty("mobilenumber.exist")));
		}
		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse(HttpStatus.PARTIAL_CONTENT.value(), environment.getProperty("email.exist")));
		}
		User user= User.builder().username(signupRequest.getUsername()).password(encoder.encode(signupRequest.getPassword()))
				.email(signupRequest.getEmail()).build();
		Set<String> strRoles = signupRequest.getRole();
		Set<Role> roles = new HashSet<>();
		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "ROLE_ADMIN":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);
					break;
				case "ROLE_MANAGER":
					Role modRole = roleRepository.findByName(ERole.ROLE_MANAGER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);
					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}
		user.setRoles(roles);
		userRepository.save(user);
		return ResponseEntity
				.ok(new MessageResponse(HttpStatus.OK.value(), environment.getProperty("registered.success"), user));
	}


	@Override
	public ResponseEntity<?> authenticateUser(@Valid LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), roles));
		
	}

}
