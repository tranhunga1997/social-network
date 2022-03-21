package com.socialnetwork.common.repositories.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.socialnetwork.common.entities.user.LoginTokenEntity;

@Repository
public interface LoginTokenRepository extends JpaRepository<LoginTokenEntity, Long>{

}
