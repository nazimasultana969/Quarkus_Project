package com.example.jwt.service;

import com.example.jwt.entity.User;
import com.example.jwt.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class UserService {

	private static final Logger LOG = Logger.getLogger(UserService.class);

	@Inject
	UserRepository userRepository;

	public List<User> getAllUsers() {

		LOG.info("Fetching all users");

		return userRepository.listAll();
	}

	public User getUser(Long id) {

		LOG.infof("Fetching user with id: %d", id);

		User user = userRepository.findById(id);

		if (user == null) {
			LOG.warnf("User not found with id: %d", id);
			throw new RuntimeException("User not found");
		}

		return user;
	}

	@Transactional
	public User createUser(User user) {

		LOG.infof("Creating user: %s", user.username);

		userRepository.persist(user);

		LOG.infof("User created successfully: %s", user.username);

		return user;
	}
}