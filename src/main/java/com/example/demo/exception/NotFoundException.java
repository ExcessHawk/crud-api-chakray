package com.example.demo.exception;

import java.util.UUID;

public class NotFoundException extends RuntimeException {

	public NotFoundException(UUID id) {
		super("User not found: " + id);
	}

}
