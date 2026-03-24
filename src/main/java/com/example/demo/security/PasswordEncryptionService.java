package com.example.demo.security;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncryptionService {

	private static final String AES = "AES";
	// AES/CBC with PKCS5 padding
	private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
	private static final int IV_LENGTH = 16;

	@Value("${app.security.aes-secret-key-hex}")
	private String hexKey;

	public String encrypt(String plainPassword) {
		if (plainPassword == null) {
			return null;
		}
		try {
			byte[] keyBytes = hexKey.trim().substring(0, 32).getBytes(StandardCharsets.UTF_8);
			SecretKeySpec secretKey = new SecretKeySpec(keyBytes, AES);
			// fixed IV for simplicity
			byte[] iv = new byte[IV_LENGTH];
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
			byte[] cipherText = cipher.doFinal(plainPassword.getBytes(StandardCharsets.UTF_8));
			ByteBuffer buffer = ByteBuffer.allocate(iv.length + cipherText.length);
			buffer.put(iv);
			buffer.put(cipherText);
			return Base64.getEncoder().encodeToString(buffer.array());
		}
		catch (GeneralSecurityException e) {
			throw new RuntimeException("Error encrypting password");
		}
	}

	public String decrypt(String encryptedPassword) {
		if (encryptedPassword == null) {
			return null;
		}
		try {
			byte[] keyBytes = hexKey.trim().substring(0, 32).getBytes(StandardCharsets.UTF_8);
			SecretKeySpec secretKey = new SecretKeySpec(keyBytes, AES);
			byte[] decoded = Base64.getDecoder().decode(encryptedPassword);
			ByteBuffer buffer = ByteBuffer.wrap(decoded);
			byte[] iv = new byte[IV_LENGTH];
			buffer.get(iv);
			byte[] cipherBytes = new byte[buffer.remaining()];
			buffer.get(cipherBytes);
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
			byte[] plain = cipher.doFinal(cipherBytes);
			return new String(plain, StandardCharsets.UTF_8);
		}
		catch (GeneralSecurityException e) {
			throw new RuntimeException("Error decrypting password");
		}
	}

}
