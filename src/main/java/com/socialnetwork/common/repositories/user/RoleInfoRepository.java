package com.socialnetwork.common.repositories.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.socialnetwork.common.entities.user.RoleInfoEntity;

@Repository
public interface RoleInfoRepository extends JpaRepository<RoleInfoEntity, Integer> {

}
