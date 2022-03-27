package com.socialnetwork.common.repositories;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.socialnetwork.common.entities.UserInfo;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserInfo, Long> {
	Optional<UserInfo> findByUsername(String username);
	
	@Query("SELECT u.id FROM #{#entityName} u WHERE u.username = ?1")
	long getUserId(String username);
	
	@Query("UPDATE #{#entityName} u SET u.enable = true WHERE u.username = ?1")
	void activeByUsername(String username);
	
	@Query("UPDATE #{#entityName} u SET u.enable = true WHERE u.id = ?1")
	void activeById(long id);
	
	@Query("UPDATE #{#entityName} u SET u.block = ?2 WHERE u.username = ?1")
	void blockAndUnblock(String username, boolean status);
	@Query("UPDATE #{#entityName} u SET u.block = ?2 WHERE u.id = ?1")
	void blockAndUnblock(long id, boolean status);
	
	@Query("UPDATE #{#entityName} u SET u.email = ?2 WHERE u.username = ?1")
	void changeEmail(String username, String email);
}
