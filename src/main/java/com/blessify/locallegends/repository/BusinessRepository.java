package com.blessify.locallegends.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.blessify.locallegends.model.Business;

public interface BusinessRepository extends JpaRepository<Business, Integer> {

	Page<Business> findByCategory(String category, Pageable pageable);

}
