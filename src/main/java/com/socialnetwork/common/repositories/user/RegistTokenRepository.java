package com.socialnetwork.common.repositories.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.socialnetwork.common.entities.user.RegistTokenEntity;

@Repository
public interface RegistTokenRepository extends JpaRepository<RegistTokenEntity, Long>{

}
