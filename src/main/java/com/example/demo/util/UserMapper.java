package com.example.demo.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.example.demo.dto.UserResponse;
import com.example.demo.model.User;

public final class UserMapper {

	private static final ZoneId MADAGASCAR = ZoneId.of("Indian/Antananarivo");
	private static final DateTimeFormatter CREATED_AT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").withZone(MADAGASCAR);

	private UserMapper() {
	}

	public static UserResponse toResponse(User user) {
		return new UserResponse(
				user.getId(),
				user.getEmail(),
				user.getName(),
				user.getPhone(),
				user.getTaxId(),
				formatCreatedAt(user.getCreatedAt()),
				user.getAddresses());
	}

	public static String formatCreatedAt(Instant instant) {
		if (instant == null) {
			return null;
		}
		return CREATED_AT.format(instant);
	}

}
