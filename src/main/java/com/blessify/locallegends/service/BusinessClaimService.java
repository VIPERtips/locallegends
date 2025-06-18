
package com.blessify.locallegends.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blessify.locallegends.dto.BusinessClaimDto;
import com.blessify.locallegends.model.Business;
import com.blessify.locallegends.model.BusinessClaim;
import com.blessify.locallegends.model.User;
import com.blessify.locallegends.repository.BusinessClaimRepository;
import com.blessify.locallegends.repository.BusinessRepository;
import com.blessify.locallegends.repository.UserRepository;

@Service
public class BusinessClaimService {

    @Autowired
    private BusinessClaimRepository businessClaimRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSender emailSender;

    public BusinessClaimDto createClaim(BusinessClaimDto dto, int businessId, int userId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found!"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        BusinessClaim claim = BusinessClaim.builder()
                .evidence(dto.getEvidence())
                .status(BusinessClaim.Status.PENDING)
                .business(business)
                .user(user)
                .build();

        BusinessClaim savedClaim = businessClaimRepository.save(claim);
        emailSender.sendAdminNotificationNewClaim(user.getEmail(), business.getName());

        return mapToDto(savedClaim);
    }

    public Page<BusinessClaimDto> getClaimsForBusiness(int page, int size) {
    	
    	
    	Sort sort = Sort.by(Sort.Direction.DESC,"submittedAt");
		Pageable pageable = PageRequest.of(page, size,sort);
        return businessClaimRepository.findAll(pageable)
        		.map(this::mapToDto);
    }
                

    public void updateClaimStatus(int claimId, BusinessClaim.Status status) {
        BusinessClaim claim = businessClaimRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Claim not found!"));
        claim.setStatus(status);
        
        if(status == BusinessClaim.Status.APPROVED){
                emailSender.sendClaimApprovalEmail(claim.getUser().getEmail(), claim.getUser().getUsername(), claim.getBusiness().getName());
        } else if(status == BusinessClaim.Status.REJECTED){
        emailSender.sendClaimRejectionEmail(claim.getUser().getEmail(), claim.getUser().getUsername(), claim.getBusiness().getName(), "");
        }
        businessClaimRepository.save(claim);
    }
    
    public BusinessClaimDto getClaimByBusinessId(int businessId) {
    	Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found!"));
        BusinessClaim claim = businessClaimRepository.findByBusiness(business);
        return mapToDto(claim);
    }

    private BusinessClaimDto mapToDto(BusinessClaim claim) {
        return BusinessClaimDto.builder()
                .id(claim.getBusinessClaimId())
                .evidence(claim.getEvidence())
                .status(claim.getStatus())
                .business(claim.getBusiness())
                .submittedAt(claim.getSubmittedAt())
                .user(claim.getUser())
                .build();
    }

	
}
