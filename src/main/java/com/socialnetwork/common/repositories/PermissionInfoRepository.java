package com.socialnetwork.common.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.socialnetwork.common.entities.PermissionInfo;

@Repository
public interface PermissionInfoRepository extends JpaRepository<PermissionInfo, Integer>{

}
