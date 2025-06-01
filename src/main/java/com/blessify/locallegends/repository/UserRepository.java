package com.blessify.locallegends.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blessify.locallegends.model.User;
import com.blessify.locallegends.model.User.Role;

public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String username);

	List<User> findByRole(Role role);

	boolean existsByEmail(String email);

}
