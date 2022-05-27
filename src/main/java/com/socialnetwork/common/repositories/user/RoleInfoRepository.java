package com.socialnetwork.common.repositories.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.socialnetwork.common.entities.user.RoleInfo;

@Repository
public interface RoleInfoRepository extends JpaRepository<RoleInfo, Integer> {
	RoleInfo findBySlug(String slug);
	
}
