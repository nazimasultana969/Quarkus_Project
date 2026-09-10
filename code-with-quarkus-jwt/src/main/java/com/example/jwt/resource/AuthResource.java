package com.example.jwt.resource;

import com.example.jwt.dto.LoginRequest;
import com.example.jwt.dto.LoginResponse;
import com.example.jwt.service.AuthService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    public LoginResponse login(LoginRequest request) {

        String token = authService.login(
                request.username(),
                request.password()
        );

        return new LoginResponse(token);
    }
}