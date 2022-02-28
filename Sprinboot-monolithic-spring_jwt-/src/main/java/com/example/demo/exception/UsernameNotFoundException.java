package com.example.demo.exception;

import javax.security.sasl.AuthenticationException;

public class UsernameNotFoundException extends AuthenticationException {

	public UsernameNotFoundException() {
		super();
		
	}

	public UsernameNotFoundException(String detail, Throwable ex) {
		super(detail, ex);
		
	}

	public UsernameNotFoundException(String detail) {
		super(detail);
		
	}
	
	

}
