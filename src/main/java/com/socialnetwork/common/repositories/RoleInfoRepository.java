package com.socialnetwork.common.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.socialnetwork.common.entities.RoleInfo;

@Repository
public interface RoleInfoRepository extends JpaRepository<RoleInfo, Integer> {

}
