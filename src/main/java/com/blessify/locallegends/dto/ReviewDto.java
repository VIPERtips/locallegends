package com.blessify.locallegends.dto;

import java.time.LocalDateTime;

import com.blessify.locallegends.model.Business;
import com.blessify.locallegends.model.User;

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
public class ReviewDto {
	private int id;
	private int rating;
	private String comment;
	private User user;
	private Business business;
	private LocalDateTime createdAt;
}
