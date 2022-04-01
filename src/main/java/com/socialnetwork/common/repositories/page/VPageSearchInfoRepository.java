package com.socialnetwork.common.repositories.page;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;

import com.socialnetwork.common.entities.page.VPageSearchInfo;

public interface VPageSearchInfoRepository extends Repository<VPageSearchInfo, Long>, JpaSpecificationExecutor<VPageSearchInfo>{
	Optional<VPageSearchInfo> findById(Long id);
	
	Page<VPageSearchInfo> findByPageName(String pageName, Pageable pageable);
}
