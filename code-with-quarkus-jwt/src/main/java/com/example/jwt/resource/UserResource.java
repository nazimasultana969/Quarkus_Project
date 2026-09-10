package com.example.jwt.resource;

import com.example.jwt.entity.User;
import com.example.jwt.service.UserService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

	@Inject
	UserService userService;

	@GET
	@RolesAllowed({ "USER", "ADMIN" })
	public List<User> getUsers() {

		return userService.getAllUsers();
	}

	@GET
	@Path("/{id}")
	@RolesAllowed({ "USER", "ADMIN" })
	public User getUser(@PathParam("id") Long id) {

		return userService.getUser(id);
	}

	@POST
	//@RolesAllowed("ADMIN")
	public User createUser(User user) {

		return userService.createUser(user);
	}
}