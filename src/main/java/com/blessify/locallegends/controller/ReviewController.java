package com.blessify.locallegends.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.blessify.locallegends.response.*;
import com.blessify.locallegends.dto.ReviewDto;
import com.blessify.locallegends.model.User;
import com.blessify.locallegends.service.JwtService;
import com.blessify.locallegends.service.ReviewService;
import com.blessify.locallegends.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserService userService;
	
	private String extractToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return (token != null && token.startsWith("Bearer ")) ? token.substring(7) : null;
    }

	@PostMapping("/{businessId}")
	public CompletableFuture<ResponseEntity<ApiResponse<ReviewDto>>> addReview(HttpServletRequest req, @RequestBody ReviewDto reviewDto,
	 @PathVariable int businessId) {
		try {
			String username = jwtService.extractUsername(extractToken(req));
			User user = userService.getUserByUsername(username);
			return reviewService.addReview(reviewDto, user.getUserId(), businessId)
					.thenApply(savedReview -> ResponseEntity.ok(new ApiResponse<>("Review added successfully", true,savedReview)));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	@GetMapping("/business/{businessId}")
	public ResponseEntity<ApiResponse<Page<ReviewDto>>> getReviewsForBusiness(@PathVariable int businessId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		try {
			CompletableFuture<Page<ReviewDto>> future = reviewService.getReviewsForBusiness(businessId, page, size);
			Page<ReviewDto> page2 = future.join();
			return ResponseEntity.ok(new ApiResponse<>("Reviews fetched successfully", true, page2));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), false, null));
		}
	}

	@DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<String>> deleteReview(@PathVariable int reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok(
                 new ApiResponse<>(
                        "Review deleted successfully",
                        false, null));
    }
}