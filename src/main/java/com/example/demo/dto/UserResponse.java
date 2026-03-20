package com.example.demo.dto;

import java.util.List;
import java.util.UUID;

import com.example.demo.model.Address;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponse(
		UUID id,
		String email,
		String name,
		String phone,
		@JsonProperty("tax_id") String taxId,
		@JsonProperty("created_at") String createdAt,
		List<Address> addresses) {
}
