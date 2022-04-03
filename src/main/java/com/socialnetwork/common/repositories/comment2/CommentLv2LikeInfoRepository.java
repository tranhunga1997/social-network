package com.socialnetwork.common.repositories.comment2;

import org.springframework.data.repository.CrudRepository;

import com.socialnetwork.common.entities.comment2.CommentLv2Info;
import com.socialnetwork.common.entities.comment2.CommentLv2LikeInfoPK;

public interface CommentLv2LikeInfoRepository extends CrudRepository<CommentLv2Info, CommentLv2LikeInfoPK>{

}
