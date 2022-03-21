package com.socialnetwork.common.repositories.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.socialnetwork.common.entities.user.LoginTokenEntity;
import com.socialnetwork.common.entities.user.pk.LoginHistioryPK;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginTokenEntity, LoginHistioryPK>{

}
