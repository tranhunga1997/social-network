package com.socialnetwork.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socialnetwork.common.repositories.user.AuthenticateRepository;
import com.socialnetwork.common.repositories.user.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthenticateRepository authenticateRepository;
	
	
}
