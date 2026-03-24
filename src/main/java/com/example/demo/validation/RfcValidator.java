package com.example.demo.validation;

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RfcValidator implements ConstraintValidator<ValidRfc, String> {

	private static final Pattern RFC = Pattern.compile(
			"^[A-ZÑ&]{3,4}\\d{6}[A-Z0-9]{3}$",
			Pattern.CASE_INSENSITIVE);

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.isBlank()) {
			return true;
		}
		return RFC.matcher(value.trim().toUpperCase()).matches();
	}

}
