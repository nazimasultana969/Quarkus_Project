package com.example.jwt.service;

import com.example.jwt.entity.User;
import com.example.jwt.repository.UserRepository;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.Set;

@ApplicationScoped
public class AuthService {

	private static final Logger LOG = Logger.getLogger(AuthService.class);

	@Inject
	UserRepository userRepository;

	public String login(String username, String password) {

		LOG.infof("Login request received for username: %s", username);

		User user = userRepository.findByUsername(username);

		if (user == null) {
			LOG.warnf("User not found: %s", username);
			throw new RuntimeException("Invalid username or password");
		}

		if (!user.password.equals(password)) {
			LOG.warnf("Invalid password for username: %s", username);
			throw new RuntimeException("Invalid username or password");
		}

		String token = Jwt.issuer("quarkus-jwt").subject(user.username).groups(Set.of(user.role)).expiresIn(3600)
				.sign();

		LOG.infof("JWT generated successfully for username: %s", username);

		return token;
	}
}