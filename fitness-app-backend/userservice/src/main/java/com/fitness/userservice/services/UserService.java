package com.fitness.userservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.models.User;
import com.fitness.userservice.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

	@Autowired
	private UserRepository repository;
	
	public UserResponse register(RegisterRequest request) {
		
		if(repository.findByEmail(request.getEmail()) != null) {
			//throw new RuntimeException("User with email " + request.getEmail() + " already exists.");
			User existingUser = repository.findByEmail(request.getEmail());
			UserResponse userResponse = new UserResponse();
			userResponse.setId(existingUser.getId());
			userResponse.setEmail(existingUser.getEmail());
			userResponse.setPassword(existingUser.getPassword());
			userResponse.setFirstName(existingUser.getFirstName());
			userResponse.setLastName(existingUser.getLastName());
			userResponse.setCreatedAt(existingUser.getCreatedAt());
			userResponse.setUpdatedAt(existingUser.getUpdatedAt());
			
			return userResponse;
		}
		User user = new User();
		user.setEmail(request.getEmail());
		user.setKeycloakId(request.getKeycloakId());
		user.setPassword(request.getPassword());
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		
		repository.save(user);
		
		UserResponse userResponse = new UserResponse();
		userResponse.setId(user.getId());
		userResponse.setEmail(user.getEmail());
		userResponse.setPassword(user.getPassword());
		userResponse.setKeycloakId(user.getKeycloakId());
		userResponse.setFirstName(user.getFirstName());
		userResponse.setLastName(user.getLastName());
		userResponse.setCreatedAt(user.getCreatedAt());
		userResponse.setUpdatedAt(user.getUpdatedAt());
		
		return userResponse;
	}

	public UserResponse getUserProfile(String userId) {
		User user = repository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
		
		UserResponse userResponse = new UserResponse();
		userResponse.setId(user.getId());
		userResponse.setEmail(user.getEmail());
		userResponse.setPassword(user.getPassword());
		userResponse.setFirstName(user.getFirstName());
		userResponse.setLastName(user.getLastName());
		userResponse.setCreatedAt(user.getCreatedAt());
		userResponse.setUpdatedAt(user.getUpdatedAt());
		return userResponse;
	}

	// checked By UserId
//	public Boolean existByUserId(String userId) {
//		log.info("calling userservice with userID: {}", userId);
//		return repository.existsById(userId);
//	}
	
	// checked By KeycloakId
	public Boolean existByUserId(String userId) {
		log.info("calling userservice with userID: {}", userId);
		return repository.existsByKeycloakId(userId);
	}

}
