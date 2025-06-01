
package com.blessify.locallegends.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.blessify.locallegends.response.*;
import com.blessify.locallegends.dto.BusinessClaimDto;
import com.blessify.locallegends.model.User;
import com.blessify.locallegends.model.BusinessClaim.Status;
import com.blessify.locallegends.service.BusinessClaimService;
import com.blessify.locallegends.service.JwtService;
import com.blessify.locallegends.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/claims")
public class BusinessClaimController {

    @Autowired
    private BusinessClaimService businessClaimService;
    
    @Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserService userService;
	
	private String extractToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return (token != null && token.startsWith("Bearer ")) ? token.substring(7) : null;
    }

    @PostMapping("/{businessId}")
    public ResponseEntity<ApiResponse<BusinessClaimDto>> createClaim(HttpServletRequest req, @RequestBody BusinessClaimDto dto, @PathVariable int businessId) {
        try {
        	String username = jwtService.extractUsername(extractToken(req));
    		User user = userService.getUserByUsername(username);
            BusinessClaimDto created = businessClaimService.createClaim(dto, businessId, user.getUserId());
            return ResponseEntity.ok(new ApiResponse<>("Claim created successfully",true, created));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>( e.getMessage(),false, null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BusinessClaimDto>>> getClaims( @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<BusinessClaimDto> claims = businessClaimService.getClaimsForBusiness(page,size);
            return ResponseEntity.ok(new ApiResponse<>("Claims fetched successfully",true,  claims));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>( e.getMessage(),false, null));
        }
    }
    
    @GetMapping("/{businessId}")
    public ResponseEntity<ApiResponse<BusinessClaimDto>> getClaims(@PathVariable int businessId) {
        try {
            BusinessClaimDto claim = businessClaimService.getClaimByBusinessId(businessId);
            return ResponseEntity.ok(new ApiResponse<>("Claim fetched successfully",true,  claim));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>( e.getMessage(),false, null));
        }
    }

    @PutMapping("/{claimId}")
    public ResponseEntity<ApiResponse<String>> updateStatus(
            @PathVariable int claimId,
            @RequestParam Status status) {
        try {
            businessClaimService.updateClaimStatus(claimId, status);
            return ResponseEntity.ok(new ApiResponse<>("Status updated!",true, status.name()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>( e.getMessage(),false, null));
        }
    }
}