package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

@Repository
public class UserRepository {

	private final List<User> users = new CopyOnWriteArrayList<>();

	public List<User> findAll() {
		return new ArrayList<>(users);
	}

	public void save(User user) {
		users.add(user);
	}

	public Optional<User> findById(UUID id) {
		return users.stream().filter(u -> u.getId().equals(id)).findFirst();
	}

	public boolean deleteById(UUID id) {
		return users.removeIf(u -> u.getId().equals(id));
	}

	public boolean existsByTaxIdExcluding(String taxId, UUID excludeId) {
		String n = taxId.trim().toUpperCase();
		return users.stream()
				.filter(u -> excludeId == null || !u.getId().equals(excludeId))
				.anyMatch(u -> u.getTaxId().equalsIgnoreCase(n));
	}

	public boolean existsByTaxId(String taxId) {
		return existsByTaxIdExcluding(taxId, null);
	}

	public Optional<User> findByTaxId(String taxId) {
		String n = taxId.trim().toUpperCase();
		return users.stream().filter(u -> u.getTaxId().equalsIgnoreCase(n)).findFirst();
	}

}
