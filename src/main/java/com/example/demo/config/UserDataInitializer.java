package com.example.demo.config;

import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.model.Address;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.PasswordEncryptionService;

@Component
public class UserDataInitializer implements CommandLineRunner {

	private static final ZoneId MADAGASCAR = ZoneId.of("Indian/Antananarivo");

	private final UserRepository repository;
	private final PasswordEncryptionService passwordEncryptionService;

	public UserDataInitializer(UserRepository repository, PasswordEncryptionService passwordEncryptionService) {
		this.repository = repository;
		this.passwordEncryptionService = passwordEncryptionService;
	}

	@Override
	public void run(String... args) {
		if (!repository.findAll().isEmpty()) {
			return;
		}
		String sharedPassword = "demo-password";
		repository.save(buildUser1(sharedPassword));
		repository.save(buildUser2(sharedPassword));
		repository.save(buildUser3(sharedPassword));
	}

	private User buildUser1(String password) {
		User u = new User();
		u.setId(UUID.randomUUID());
		u.setEmail("user1@mail.com");
		u.setName("user1");
		u.setPhone("+1 55 555 555 55");
		u.setEncryptedPassword(passwordEncryptionService.encrypt(password));
		u.setTaxId("AARR990101XXX");
		u.setCreatedAt(ZonedDateTime.of(2026, 1, 1, 0, 0, 0, 0, MADAGASCAR).toInstant());
		u.setAddresses(List.of(
				new Address(1, "workaddress", "street No. 1", "UK"),
				new Address(2, "homeaddress", "street No. 2", "AU")));
		return u;
	}

	private User buildUser2(String password) {
		User u = new User();
		u.setId(UUID.randomUUID());
		u.setEmail("user2@mail.com");
		u.setName("user2");
		u.setPhone("+52 55 5555 5555");
		u.setEncryptedPassword(passwordEncryptionService.encrypt(password));
		u.setTaxId("BOBB850101XXX");
		u.setCreatedAt(ZonedDateTime.of(2026, 2, 1, 12, 30, 0, 0, MADAGASCAR).toInstant());
		u.setAddresses(List.of(
				new Address(1, "workaddress", "street No. 3", "MX"),
				new Address(2, "homeaddress", "street No. 4", "US")));
		return u;
	}

	private User buildUser3(String password) {
		User u = new User();
		u.setId(UUID.randomUUID());
		u.setEmail("user3@mail.com");
		u.setName("user3");
		u.setPhone("+1 555 555 1234");
		u.setEncryptedPassword(passwordEncryptionService.encrypt(password));
		u.setTaxId("CAPC900101XXX");
		u.setCreatedAt(ZonedDateTime.of(2026, 3, 1, 8, 15, 0, 0, MADAGASCAR).toInstant());
		u.setAddresses(List.of(
				new Address(1, "workaddress", "street No. 5", "DE"),
				new Address(2, "homeaddress", "street No. 6", "FR")));
		return u;
	}

}
