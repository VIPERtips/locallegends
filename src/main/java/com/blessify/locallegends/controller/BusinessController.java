package com.blessify.locallegends.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blessify.locallegends.dto.BusinessDto;
import com.blessify.locallegends.model.Business;
import com.blessify.locallegends.model.User;
import com.blessify.locallegends.response.ApiResponse;
import com.blessify.locallegends.service.BusinessService;
import com.blessify.locallegends.service.JwtService;
import com.blessify.locallegends.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/business")
public class BusinessController {

	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserService userService;
	
	private String extractToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return (token != null && token.startsWith("Bearer ")) ? token.substring(7) : null;
    }
	
	@PostMapping
	public ResponseEntity<ApiResponse<BusinessDto>> addBusiness(@RequestBody BusinessDto dto,HttpServletRequest req){
	try {
		String username = jwtService.extractUsername(extractToken(req));
		User user = userService.getUserByUsername(username);
		BusinessDto addedBusiness = businessService.addBusiness(dto, user.getUserId());
		return ResponseEntity.ok(new ApiResponse<>("Business added successfully",true,addedBusiness));
	} catch (Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(e.getMessage(), false, null));
	}	
	}
	
	@GetMapping
	public ResponseEntity<ApiResponse<Page<BusinessDto>>> getAllBusinessedEntity(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
		try {
			Page<BusinessDto> business = businessService.getAllBusiness(page,size);
			return ResponseEntity.ok(new ApiResponse<>("Business(s) retrieved successfully",true,business));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body(new ApiResponse<>(e.getMessage(), false, null));
		}	
	}
	
	@GetMapping("/top-rated")
	public ResponseEntity<Page<BusinessDto>> getTopRatedBusinesses(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {
	    Page<BusinessDto> topRated = businessService.getTopRatedBusinesses(page, size);
	    return ResponseEntity.ok(topRated);
	}
	
	@GetMapping("/top-rated/category")
	public ResponseEntity<Page<BusinessDto>> getTopRatedBusinessesByCategory(
	        @RequestParam String category,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {
	    Page<BusinessDto> topRatedByCategory = businessService.getTopRatedBusinessesByCategory(category, page, size);
	    return ResponseEntity.ok(topRatedByCategory);
	}


	
	@GetMapping("/{businessId}")
	public ResponseEntity<ApiResponse<BusinessDto>> getAllBusinessedEntity(@PathVariable int businessId){
		try {
			BusinessDto business = businessService.getBusinessById(businessId);
			return ResponseEntity.ok(new ApiResponse<>("Business retrieved successfully",true,business));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body(new ApiResponse<>(e.getMessage(), false, null));
		}	
	}
	
	@PutMapping("/{businessId}")
	public ResponseEntity<ApiResponse<BusinessDto>> getAllBusinessedEntity(@PathVariable int businessId,@RequestBody BusinessDto dto,HttpServletRequest req){
		try {
			String username = jwtService.extractUsername(extractToken(req));
			User user = userService.getUserByUsername(username);
			BusinessDto business = businessService.update(dto, user.getUserId(), businessId);
			return ResponseEntity.ok(new ApiResponse<>("Business updated successfully",true,business));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body(new ApiResponse<>(e.getMessage(), false, null));
		}	
	}
	
	@DeleteMapping("/{businessId}")
	public ResponseEntity<ApiResponse<BusinessDto>> deleteBusiness(@PathVariable int businessId){
		try {
			businessService.deleteBusiness(businessId);
			return ResponseEntity.ok(new ApiResponse<>("Business deleted successfully",true,null));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body(new ApiResponse<>(e.getMessage(), false, null));
		}	
	}
	
}
