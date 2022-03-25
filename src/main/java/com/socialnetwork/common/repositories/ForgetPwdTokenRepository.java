package com.socialnetwork.common.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.socialnetwork.common.entities.ForgetPwdTokenInfo;

@Repository
public interface ForgetPwdTokenRepository extends JpaRepository<ForgetPwdTokenInfo, Long> {

}
