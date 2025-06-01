package com.blessify.locallegends.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
	private int id;
	private String firstName,lastName;
	private String email;
	private String password;
	private String confirmPassword;
}
