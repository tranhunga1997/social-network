package com.socialnetwork.common.configs;

import java.time.LocalDateTime;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;

import com.socialnetwork.common.entities.sequence.SequenceInfo;
import com.socialnetwork.common.services.sequence.SequenceService;
import com.socialnetwork.common.utils.MessageUtils;
import com.socialnetwork.general.user.dtos.UserDetailInfoDto;
import com.socialnetwork.general.user.services.impl.UserService;

@Configuration
public class DataInit {
	
	@Autowired
	private UserService userSerivce;
	@Autowired
	private SequenceService sequenceService;
	//@PostConstruct
	public void initUserData() {
		if(userSerivce.findAll(PageRequest.of(0, 2)).getTotalElements() != 0) {
			return;
		}
		for(int i=1; i<=10; i++) {
			UserDetailInfoDto dto = new UserDetailInfoDto();
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
