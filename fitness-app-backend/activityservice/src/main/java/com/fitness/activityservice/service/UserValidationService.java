package com.fitness.activityservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserValidationService {

	private final WebClient userServiceWebClient;
	
	public boolean isUserValid(String userId) {
		log.info("calling userservice with userID: {}", userId);
		try {
			Boolean isValid = userServiceWebClient.get()
					.uri("/api/users/{userId}/validate", userId)
					.retrieve()
					.bodyToMono(Boolean.class)
					.block();
			return isValid != null && isValid;
		} catch (WebClientResponseException e) {
			e.printStackTrace();
			return false;
		}
		
	}
}
