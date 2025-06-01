package com.blessify.locallegends.response;

import com.blessify.locallegends.model.User;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private String token;
    private String role;
    private String refreshToken;
    private String message;
    private String status;
    private Integer id;
	private User user;
}
