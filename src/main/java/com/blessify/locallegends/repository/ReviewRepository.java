package com.blessify.locallegends.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.blessify.locallegends.model.Business;
import com.blessify.locallegends.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer>{

	Page<Review> findByBusiness(Business business, Pageable pageable);

	List<Review> findByBusiness(Business business);

	int countByBusiness(Business business);

}
