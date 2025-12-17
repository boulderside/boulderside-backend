package com.line7studio.boulderside.common.security.provider;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.line7studio.boulderside.common.exception.ErrorCode;
import com.line7studio.boulderside.common.exception.ExternalException;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AESProvider {
	@Value("${aes.secret-key}")
	private String secretKey;

	@Value("${aes.init-vector}")
	private String initVector;

	private SecretKeySpec secretKeySpec;
	private IvParameterSpec ivSpec;

	@PostConstruct
	private void init() throws UnsupportedEncodingException {
		// AES 키와 IV 초기화
		secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
		ivSpec = new IvParameterSpec(initVector.getBytes());
	}

	public String encrypt(String plainText) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
			byte[] encrypted = cipher.doFinal(plainText.getBytes());
			return Base64.getEncoder().encodeToString(encrypted);
		} catch (Exception ex) {
			throw new ExternalException(ErrorCode.AES_ENCRYPTION_ERROR);
		}
	}

	public String decrypt(String encryptedText) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
			byte[] original = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
			return new String(original);
		} catch (Exception ex) {
			throw new ExternalException(ErrorCode.AES_DECRYPTION_ERROR);
		}
	}
}
