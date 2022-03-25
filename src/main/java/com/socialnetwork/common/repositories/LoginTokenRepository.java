package com.socialnetwork.common.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.socialnetwork.common.entities.LoginTokenEntity;

@Repository
public interface LoginTokenRepository extends JpaRepository<LoginTokenEntity, Long>{

}