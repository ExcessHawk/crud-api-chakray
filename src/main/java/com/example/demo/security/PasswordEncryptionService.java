package com.example.demo.security;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncryptionService {

	private static final String AES = "AES";
	private static final String TRANSFORMATION = "AES/GCM/NoPadding";
	private static final int GCM_IV_LENGTH = 12;
	private static final int GCM_TAG_LENGTH = 128;

	private final SecretKey secretKey;

	public PasswordEncryptionService(@Value("${app.security.aes-secret-key-hex}") String hexKey) {
		byte[] keyBytes = HexFormat.of().parseHex(hexKey.trim());
		if (keyBytes.length != 32) {
			throw new IllegalArgumentException("AES key must be 256 bits (32 bytes, 64 hex characters)");
		}
		this.secretKey = new SecretKeySpec(keyBytes, AES);
	}

	public String encrypt(String plainPassword) {
		if (plainPassword == null) {
			return null;
		}
		try {
			byte[] iv = new byte[GCM_IV_LENGTH];
			new SecureRandom().nextBytes(iv);
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(GCM_TAG_LENGTH, iv));
			byte[] cipherText = cipher.doFinal(plainPassword.getBytes(StandardCharsets.UTF_8));
			ByteBuffer buffer = ByteBuffer.allocate(iv.length + cipherText.length);
			buffer.put(iv);
			buffer.put(cipherText);
			return Base64.getEncoder().encodeToString(buffer.array());
		}
		catch (GeneralSecurityException e) {
			throw new IllegalStateException("Failed to encrypt password", e);
		}
	}

	public String decrypt(String encryptedPassword) {
		if (encryptedPassword == null) {
			return null;
		}
		try {
			byte[] decoded = Base64.getDecoder().decode(encryptedPassword);
			ByteBuffer buffer = ByteBuffer.wrap(decoded);
			byte[] iv = new byte[GCM_IV_LENGTH];
			buffer.get(iv);
			byte[] cipherBytes = new byte[buffer.remaining()];
			buffer.get(cipherBytes);
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(GCM_TAG_LENGTH, iv));
			byte[] plain = cipher.doFinal(cipherBytes);
			return new String(plain, StandardCharsets.UTF_8);
		}
		catch (GeneralSecurityException e) {
			throw new IllegalStateException("Failed to decrypt password", e);
		}
	}

}
