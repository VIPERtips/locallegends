package com.blessify.locallegends.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessDto {
	private int id;
	private String name;
	private String description;
	private String category;
	private String contactInfo;
	private String location;
	private String address, city, state, zipCode, phone, email, website;
	private int averageRating;
	private int reviewCount;
}
