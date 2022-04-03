package com.socialnetwork.common.repositories.user;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.socialnetwork.common.entities.user.UserInfo;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserInfo, Long> {
	Optional<UserInfo> findByUsername(String username);
	Optional<UserInfo> findByEmail(String email);
	
	@Modifying
	@Query("UPDATE #{#entityName} u SET u.isEnabled = true, u.updateAt = NOW() WHERE u.username = :username")
	void activeByUsername(String username);
	
	@Modifying
	@Query("UPDATE #{#entityName} u SET u.isEnabled = true, u.updateAt = NOW() WHERE u.userId = ?1")
	void activeById(long id);
	
	@Modifying
	@Query("UPDATE #{#entityName} u SET u.isBlocked = ?2, u.updateAt = NOW() WHERE u.username = ?1")
	void blockAndUnblock(String username, boolean status);
	@Modifying
	@Query("UPDATE #{#entityName} u SET u.isBlocked = ?2, u.updateAt = NOW() WHERE u.userId = ?1")
	void blockAndUnblock(long id, boolean status);
	@Modifying
	@Query("UPDATE #{#entityName} u SET u.email = ?2, u.updateAt = NOW() WHERE u.username = ?1")
	void changeEmail(String username, String email);
	
	@Query("SELECT u.userId FROM #{#entityName} u WHERE u.username = ?1")
	long getUserId(String username);
	
	boolean existsByUsername(String username);
	
	boolean existsByEmail(String email);
}
