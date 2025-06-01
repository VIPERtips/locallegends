package com.blessify.locallegends.model;

import java.time.LocalDateTime;

import com.blessify.locallegends.model.User.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class BusinessClaim {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int businessClaimId;
	@JsonIgnore
	@OneToOne
	@JoinColumn(referencedColumnName = "businessId",name = "business_id",nullable = false)
	private Business business;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(referencedColumnName = "userId",name = "user_id",nullable = false)
	private User user;
	private Status status;
	private String evidence;  //docs
	private LocalDateTime submittedAt;
	@PrePersist
	protected void onCreate() {
		submittedAt = LocalDateTime.now();
	}
	
	public enum Status {
		PENDING,APPROVED,REJECTED
	}
}
