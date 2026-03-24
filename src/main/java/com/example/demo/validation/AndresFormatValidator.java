package com.example.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AndresFormatValidator implements ConstraintValidator<AndresFormat, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		if (value.isBlank()) {
			return false;
		}
		String digits = value.replaceAll("\\D", "");
		if (digits.length() < 10) {
			return false;
		}
		String national = digits.length() > 10 ? digits.substring(digits.length() - 10) : digits;
		if (national.length() != 10) {
			return false;
		}
		char first = national.charAt(0);
		if (first == '0' || first == '1') {
			return false;
		}
		return true;
	}

}
