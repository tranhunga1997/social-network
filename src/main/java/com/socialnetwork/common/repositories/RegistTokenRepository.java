package com.socialnetwork.common.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.socialnetwork.common.entities.RegistTokenInfo;

@Repository
public interface RegistTokenRepository extends JpaRepository<RegistTokenInfo, Long>{

}
