package com.blessify.locallegends.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blessify.locallegends.dto.ReviewDto;
import com.blessify.locallegends.model.Business;
import com.blessify.locallegends.model.Review;
import com.blessify.locallegends.model.User;
import com.blessify.locallegends.repository.BusinessRepository;
import com.blessify.locallegends.repository.ReviewRepository;

@Service
public class ReviewService {
	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private BusinessRepository businessRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private EmailSender emailSender;
	
	public CompletableFuture<ReviewDto> addReview(ReviewDto req, int userId, int businessId){
		return CompletableFuture.supplyAsync(()-> {
			User user = userService.getUserBy_Id(userId);
			Business business = businessRepository.findById(businessId).orElseThrow(()->
			new RuntimeException("No business found with id"+businessId));
			
			Review review = Review.builder()
					.user(user)
					.business(business)
					.rating(req.getRating())
					.comment(req.getComment())
					.build();
			
			reviewRepository.save(review);
			
			List<Review> reviews = reviewRepository.findByBusiness(business);
			double avgRating = reviews.stream()
					.mapToInt(Review::getRating)
					.average()
					.orElse(0.0);
			business.setAverageRating((int) avgRating);
			businessRepository.save(business);

			emailSender.sendNewReviewEmail(business.getUser().getEmail(), business.getName(), user.getUsername(), req.getRating(), req.getComment());
			
			return ReviewDto.builder()
					.comment(review.getComment())
					.rating(review.getRating())
					.user(review.getUser())
					.business(review.getBusiness())
					.build();
					
		});
	}
	
	public CompletableFuture<Page<ReviewDto>> getReviewsForBusiness(int businessId, int page, int size){
		Business business = businessRepository.findById(businessId).orElseThrow(()->
		new RuntimeException("No business found with id"+businessId));
		
		Sort sort = Sort.by(Sort.Direction.DESC,"createdAt");
		Pageable pageable = PageRequest.of(page, size,sort);
		return CompletableFuture.supplyAsync(()-> {
			return reviewRepository.findByBusiness(business,pageable)
					.map(r -> ReviewDto.builder()
							.id(r.getReviewId())
							.comment(r.getComment())
							.rating(r.getRating())
							.user(r.getUser())
							.business(r.getBusiness())
							.createdAt(r.getCreatedAt())
							.build())
					;
		});
	}
	
	public void deleteReview(int reviewId) {
		if(!reviewRepository.existsById(reviewId)) {
			throw new RuntimeException("Review not found");
			
		}
		reviewRepository.deleteById(reviewId);
	}


}
