package com.example.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CreateUserRequest;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.exception.BadRequestException;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@RestController
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/users")
	public List<UserResponse> list(
			@RequestParam(required = false) String sortedBy,
			@RequestParam(required = false) String filter) {
		if (filter != null && filter.isBlank()) {
			throw new BadRequestException("filter must not be empty or null when provided");
		}
		return userService.list(sortedBy, filter);
	}

	@PostMapping("/users")
	@ResponseStatus(HttpStatus.CREATED)
	public UserResponse create(@Valid @RequestBody CreateUserRequest request) {
		return userService.create(request);
	}

	@PatchMapping("/users/{id}")
	public UserResponse patch(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest request) {
		return userService.update(id, request);
	}

	@DeleteMapping("/users/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable UUID id) {
		userService.delete(id);
	}

}
