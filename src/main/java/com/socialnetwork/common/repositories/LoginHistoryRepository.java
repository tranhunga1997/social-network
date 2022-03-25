package com.socialnetwork.common.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.socialnetwork.common.entities.LoginTokenEntity;
import com.socialnetwork.common.entities.pk.LoginHistioryPK;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginTokenEntity, LoginHistioryPK>{

}
