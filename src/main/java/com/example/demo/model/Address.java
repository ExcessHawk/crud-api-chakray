package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record Address(
		@Positive int id,
		@NotBlank String name,
		@NotBlank String street,
		@NotBlank @JsonProperty("country_code") String countryCode) {
}
