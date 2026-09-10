package com.example.jwt;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

public class KeyGenerator {

	public static void main(String[] args) throws Exception {

		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");

		generator.initialize(2048);

		KeyPair keyPair = generator.generateKeyPair();

		String privateKey = "-----BEGIN PRIVATE KEY-----\n"
				+ Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(keyPair.getPrivate().getEncoded())
				+ "\n-----END PRIVATE KEY-----\n";

		String publicKey = "-----BEGIN PUBLIC KEY-----\n"
				+ Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(keyPair.getPublic().getEncoded())
				+ "\n-----END PUBLIC KEY-----\n";

		Path resources = Path.of("src/main/resources");

		Files.createDirectories(resources);

		try (FileWriter writer = new FileWriter(resources.resolve("privateKey.pem").toFile())) {
			writer.write(privateKey);
		}

		try (FileWriter writer = new FileWriter(resources.resolve("publicKey.pem").toFile())) {
			writer.write(publicKey);
		}

		System.out.println("JWT keys generated successfully.");
	}
}