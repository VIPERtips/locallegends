package com.blessify.locallegends.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.blessify.locallegends.model.Business;
import com.blessify.locallegends.model.BusinessClaim;

public interface BusinessClaimRepository extends JpaRepository<BusinessClaim, Integer> {

	Page<BusinessClaim> findByBusiness(Business business, Pageable pageable);

	BusinessClaim findByBusiness(Business business);

}
