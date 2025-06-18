package com.blessify.locallegends.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blessify.locallegends.dto.BusinessDto;
import com.blessify.locallegends.model.Business;
import com.blessify.locallegends.model.User;
import com.blessify.locallegends.repository.BusinessRepository;
import com.blessify.locallegends.repository.ReviewRepository;
import com.blessify.locallegends.repository.UserRepository;

@Service
public class BusinessService {
	@Autowired
	private BusinessRepository businessRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	public BusinessDto addBusiness(BusinessDto req,int userId) {
		User user = userRepository.findById(userId).orElseThrow(()->
		new RuntimeException("No user found with idc"+userId));
		Business business = Business.builder()
				.name(req.getName())
				.description(req.getDescription())
				.location(req.getLocation())
				.address(req.getAddress())
				.category(req.getCategory())
				.contactInfo(req.getContactInfo())
				.email(req.getEmail())
				.city(req.getCity())
				.state(req.getState())
				.zipCode(req.getZipCode())
				.user(user)
				.phone(req.getPhone())
				.website(req.getWebsite())
		.build();
		
		Business savedBusiness = businessRepository.save(business);
		return mapToDto(savedBusiness);
	}
	
	public BusinessDto update(BusinessDto req,int userId,int businessId) {
		User user = userRepository.findById(userId).orElseThrow(()->
		new RuntimeException("No user found with id"+userId));
		Business business = businessRepository.findById(businessId).orElseThrow(()->
		new RuntimeException("No business found with id"+businessId));
		business.setUser(user);
		business.setName(req.getName());
		business.setDescription(req.getDescription());
		business.setLocation(req.getLocation());
		business.setAddress(req.getAddress());
		business.setCategory(req.getCategory());
		business.setContactInfo(req.getContactInfo());
		business.setEmail(req.getEmail());
		business.setCity(req.getCity());
		business.setState(req.getState());
		business.setZipCode(req.getZipCode());
		business.setPhone(req.getPhone());
		business.setWebsite(req.getWebsite());
		
		Business updatedBusiness =  businessRepository.save(business);
		return mapToDto(updatedBusiness);
	}

	public BusinessDto getBusinessById(int businessId) {
		Business business = businessRepository.findById(businessId).orElseThrow(()->
		new RuntimeException("No business found with id"+businessId));
		return mapToDto(business);
	}
	
	public void deleteBusiness(int businessId) {
		Business business = businessRepository.findById(businessId).orElseThrow(()->
		new RuntimeException("No business found with id"+businessId));

		businessRepository.delete(business);
	}
	
	public Page<BusinessDto> getAllBusiness(int page,int size){
		Sort sort = Sort.by(Sort.Direction.DESC,"createdAt");
		Pageable pageable = PageRequest.of(page, size,sort);
		return businessRepository.findAll(pageable)
				.map(this::mapToDto);
	}
	
	private BusinessDto mapToDto(Business business) {
		int reviewCount = reviewRepository.countByBusiness(business);
		return BusinessDto.builder()
				.id(business.getBusinessId())
				.name(business.getName())
				.description(business.getDescription())
				.location(business.getLocation())
				.address(business.getAddress())
				.category(business.getCategory())
				.contactInfo(business.getContactInfo())
				.email(business.getEmail())
				.city(business.getCity())
				.state(business.getState())
				.zipCode(business.getZipCode())
				.phone(business.getPhone())
				.website(business.getWebsite())
				.averageRating(business.getAverageRating())
				.reviewCount(reviewCount)
				.build();
	}
	
	/*public Page<BusinessDto> getTopRatedBusinesses(int page, int size) {
	    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "averageRating"));
	    return businessRepository.findAll(pageable)
	            .map(this::mapToDto);
	}
	public Page<BusinessDto> getTopRatedBusinessesByCategory(String category, int page, int size) {
	    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "averageRating"));
	    return businessRepository.findByCategory(category, pageable)
	            .map(this::mapToDto);
	}*/

	public Page<BusinessDto> getTopRatedBusinesses(int page, int size) {  Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "averageRating"));
	 return businessRepository.findTopRatedBusinesses(3.0, pageable)
	 	.map(this::mapToDto);
	} 
	public Page<BusinessDto> getTopRatedBusinessesByCategory(String category, int page, int size) { 	
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "averageRating"));return businessRepository.findTopRatedBusinessesByCategory(category, 3.0, pageable)
		.map(this::mapToDto); 
	} 



	public Page<BusinessDto> searchBusinesses(String name, String location, String category, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Business> results = businessRepository.findByNameContainingIgnoreCaseAndLocationContainingIgnoreCaseAndCategoryContainingIgnoreCase(name == null ? "" : name,location == null ? "" : location,category == null ? "" : category,pageable);
		return results.map(this::mapToDto);
}


}
