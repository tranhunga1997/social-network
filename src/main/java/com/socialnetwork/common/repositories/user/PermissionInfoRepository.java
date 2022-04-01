package com.socialnetwork.common.repositories.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.socialnetwork.common.entities.user.PermissionInfo;

@Repository
public interface PermissionInfoRepository extends JpaRepository<PermissionInfo, Integer>{

}
