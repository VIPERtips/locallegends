package com.blessify.locallegends.model;

import java.time.LocalDateTime;
import java.util.List;

import com.blessify.locallegends.model.User.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Business {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int businessId;
	private String name;
	private String description;
	private String category;
	private String contactInfo;
	private String location;
	private String address, city, state, zipCode, phone, email, website;
	@JsonIgnore
	@ManyToOne
	@JoinColumn(referencedColumnName = "userId", name = "user_id", nullable = false)
	private User user;
	private int averageRating;
	private int reviewCount;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	@OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Review> reviews;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}
}
