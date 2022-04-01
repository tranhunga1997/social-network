package com.socialnetwork.common.configs;

import java.time.LocalDateTime;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.socialnetwork.common.entities.sequence.SequenceInfo;
import com.socialnetwork.common.services.sequence.SequenceService;
import com.socialnetwork.user.dtos.UserInfoDto;
import com.socialnetwork.user.services.UserService;

@Configuration
public class DataInit {
	@Autowired
	private UserService userSerivce;
	@Autowired
	private SequenceService sequenceService;
	//@PostConstruct
	public void initUserData() {
		if(userSerivce.findAll().size() != 0) {
			return;
		}
		for(int i=1; i<=10; i++) {
			UserInfoDto dto = new UserInfoDto();
			//dto.setUserId(sequenceService.getNextId("USER_ID_SEQ"));
			dto.setUsername("sunico"+i);
			dto.setEnable(true);
			dto.setBlock(false);
			dto.setLastName("Hùng "+i);
			dto.setFirstName("Mạnh");
			dto.setEmail("tranhung"+i+"@gmail.com");
			dto.setCreateDatetime(LocalDateTime.now());
			userSerivce.create(dto);
		}
	}
}
