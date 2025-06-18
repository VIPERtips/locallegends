package com.blessify.locallegends.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blessify.locallegends.model.Business;

public interface BusinessRepository extends JpaRepository<Business, Integer> {

	Page<Business> findByCategory(String category, Pageable pageable);
    
    @Query("SELECT b FROM Business b WHERE b.averageRating IS NOT NULL AND b.averageRating >= :rating") 
    Page<Business> findTopRatedBusinesses(@Param("rating") double rating, Pageable pageable); 
    @Query("SELECT b FROM Business b WHERE b.category = :category AND b.averageRating IS NOT NULL AND b.averageRating >= :rating") Page<Business> findTopRatedBusinessesByCategory(@Param("category") String category, @Param("rating") double rating, Pageable pageable); 



    Page<Business> findByName(String name,Pageable pageable);

   Page<Business> findByNameContainingIgnoreCaseAndLocationContainingIgnoreCaseAndCategoryContainingIgnoreCase(String name,String location,String category,Pageable pageable
);

}
