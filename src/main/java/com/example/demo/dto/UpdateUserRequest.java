package com.example.demo.dto;

import java.util.List;

import com.example.demo.model.Address;
import com.example.demo.validation.AndresFormat;
import com.example.demo.validation.ValidRfc;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

public class UpdateUserRequest {

	@Email
	private String email;

	private String name;

	@AndresFormat
	private String phone;

	private String password;

	@ValidRfc
	@JsonProperty("tax_id")
	private String taxId;

	@Valid
	private List<Address> addresses;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

}
