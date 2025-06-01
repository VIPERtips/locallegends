package com.blessify.locallegends.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
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
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int reviewId;
	private int rating;
	private String comment;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(referencedColumnName = "businessId",name = "business_id",nullable = false)
	private Business business;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(referencedColumnName = "userId",name = "user_id",nullable = false)
	private User user;
	private LocalDateTime createdAt;
	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
	}
}
