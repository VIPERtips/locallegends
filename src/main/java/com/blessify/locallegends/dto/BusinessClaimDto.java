package com.blessify.locallegends.dto;

import com.blessify.locallegends.model.User;

import java.time.LocalDateTime;

import com.blessify.locallegends.model.Business;
import com.blessify.locallegends.model.BusinessClaim.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessClaimDto {
	private Business business;
	private int id;
	private User user;
	private Status status;
	private String evidence; 
	private LocalDateTime submittedAt;
}
