package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageResponse {
	
	private int status;
	private String message;
	private Object object;
	public MessageResponse(int status, String message, Object object) {
		super();
		this.status = status;
		this.message = message;
		this.object = object;
	}
	public MessageResponse(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
	
}