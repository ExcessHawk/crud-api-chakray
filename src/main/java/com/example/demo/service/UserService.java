package com.example.demo.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CreateUserRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.PasswordEncryptionService;
import com.example.demo.util.UserMapper;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;
	@Autowired
	private PasswordEncryptionService passwordEncryptionService;

	public UserService() {
	}

	public List<UserResponse> list(String sortedBy, String filter) {
		List<User> list = new ArrayList<>(repository.findAll());
		if (filter != null && !filter.isBlank()) {
			list = list.stream().filter(u -> matchesFilter(u, filter)).toList();
		}
		if (sortedBy != null && !sortedBy.isBlank()) {
			Comparator<User> cmp = comparatorFor(sortedBy.trim().toLowerCase(Locale.ROOT));
			list = new ArrayList<>(list);
			list.sort(cmp);
		}
		return list.stream().map(UserMapper::toResponse).toList();
	}

	public UserResponse create(CreateUserRequest req) {
		if (repository.existsByTaxId(req.getTaxId())) {
			throw new ConflictException("tax_id is already registered");
		}
		User u = new User();
		u.setId(UUID.randomUUID());
		u.setEmail(req.getEmail().trim());
		u.setName(req.getName().trim());
		u.setPhone(req.getPhone().trim());
		u.setEncryptedPassword(passwordEncryptionService.encrypt(req.getPassword()));
		u.setTaxId(req.getTaxId().trim().toUpperCase(Locale.ROOT));
		u.setCreatedAt(Instant.now());
		u.setAddresses(new ArrayList<>(req.getAddresses()));
		repository.save(u);
		return UserMapper.toResponse(u);
	}

	public UserResponse update(UUID id, UpdateUserRequest req) {
		User u = repository.findById(id).orElseThrow(() -> new NotFoundException(id));
		if (req.getEmail() != null) {
			u.setEmail(req.getEmail().trim());
		}
		if (req.getName() != null) {
			u.setName(req.getName().trim());
		}
		if (req.getPhone() != null) {
			u.setPhone(req.getPhone().trim());
		}
		if (req.getPassword() != null) {
			u.setEncryptedPassword(passwordEncryptionService.encrypt(req.getPassword()));
		}
		if (req.getTaxId() != null) {
			String next = req.getTaxId().trim().toUpperCase(Locale.ROOT);
			if (repository.existsByTaxIdExcluding(next, id)) {
				throw new ConflictException("tax_id is already registered");
			}
			u.setTaxId(next);
		}
		if (req.getAddresses() != null) {
			u.setAddresses(new ArrayList<>(req.getAddresses()));
		}
		return UserMapper.toResponse(u);
	}

	public void delete(UUID id) {
		if (!repository.deleteById(id)) {
			throw new NotFoundException(id);
		}
	}

	public LoginResponse login(LoginRequest req) {
		User u = repository.findByTaxId(req.getTaxId())
				.orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
		String decrypted = passwordEncryptionService.decrypt(u.getEncryptedPassword());
		if (!decrypted.equals(req.getPassword())) {
			throw new UnauthorizedException("Invalid credentials");
		}
		return new LoginResponse(u.getId(), "Authenticated");
	}

	private boolean matchesFilter(User u, String filter) {
		String[] parts = filter.split("\\+", 3);
		if (parts.length != 3) {
			throw new IllegalArgumentException("filter must be field+op+value (use + as separator)");
		}
		String field = parts[0].trim().toLowerCase(Locale.ROOT);
		String op = parts[1].trim().toLowerCase(Locale.ROOT);
		String value = parts[2];
		String haystack = fieldValueForFilter(u, field);
		if (haystack == null) {
			return false;
		}
		return switch (op) {
			case "co" -> haystack.toLowerCase(Locale.ROOT).contains(value.toLowerCase(Locale.ROOT));
			case "eq" -> field.equals("tax_id") || field.equals("email")
					? haystack.equalsIgnoreCase(value)
					: haystack.equals(value);
			case "sw" -> haystack.startsWith(value);
			case "ew" -> haystack.endsWith(value);
			default -> throw new IllegalArgumentException("Unsupported filter operator: " + op);
		};
	}

	private String fieldValueForFilter(User u, String field) {
		return switch (field) {
			case "id" -> u.getId().toString();
			case "email" -> u.getEmail();
			case "name" -> u.getName();
			case "phone" -> nationalDigits(u.getPhone());
			case "tax_id" -> u.getTaxId();
			case "created_at" -> UserMapper.formatCreatedAt(u.getCreatedAt());
			default -> throw new IllegalArgumentException("Unsupported filter field: " + field);
		};
	}

	private static String nationalDigits(String phone) {
		String digits = phone.replaceAll("\\D", "");
		if (digits.length() > 10) {
			return digits.substring(digits.length() - 10);
		}
		return digits;
	}

	private Comparator<User> comparatorFor(String sortedBy) {
		return switch (sortedBy) {
			case "email" -> Comparator.comparing(u -> u.getEmail().toLowerCase(Locale.ROOT));
			case "id" -> Comparator.comparing(User::getId, Comparator.naturalOrder());
			case "name" -> Comparator.comparing(u -> u.getName().toLowerCase(Locale.ROOT));
			case "phone" -> Comparator.comparing(User::getPhone, Comparator.naturalOrder());
			case "tax_id" -> Comparator.comparing(u -> u.getTaxId().toLowerCase(Locale.ROOT));
			case "created_at" -> Comparator.comparing(User::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder()));
			default -> throw new IllegalArgumentException("Unsupported sortedBy: " + sortedBy);
		};
	}

}
