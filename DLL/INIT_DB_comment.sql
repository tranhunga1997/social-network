-- DROP TABLE
SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS m_comment_lv1_info CASCADE;
DROP TABLE IF EXISTS m_comment_lv2_info CASCADE;
DROP TABLE IF EXISTS m_comment_lv1_like_info CASCADE;
DROP TABLE IF EXISTS m_comment_lv2_like_info CASCADE;

CREATE TABLE m_comment_lv1_info (
    page_id BIGINT NOT NULL, 
    status_id BIGINT NOT NULL, 
    content_id INTEGER NOT NULL, 
    comment_lv1_id BIGINT NOT NULL,
    owner_id BIGINT, 
    content_text VARCHAR(2000), 
    content_type CHAR(1), 
    content_uri VARCHAR(255), 
    block_at TIMESTAMP(6),
    block_cause_id INTEGER, 
    block_flag CHAR(1) NOT NULL, 
    create_at TIMESTAMP(6), 
    del_at TIMESTAMP(6), 
    del_flag CHAR(1) NOT NULL, 
    update_at TIMESTAMP(6), 
    
    PRIMARY KEY (page_id, status_id, content_id, comment_lv1_id )
);
CREATE TABLE m_comment_lv1_like_info (
    page_id BIGINT NOT NULL, 
    status_id BIGINT NOT NULL, 
    content_id INTEGER NOT NULL, 
    comment_lv1_id BIGINT NOT NULL, 
    user_id BIGINT NOT NULL, 
    like_type CHAR(1), 
    create_at TIMESTAMP(6), 
    PRIMARY KEY (page_id, status_id, content_id, comment_lv1_id, user_id )
    );
CREATE TABLE m_comment_lv2_info (
    page_id BIGINT NOT NULL, 
    status_id BIGINT NOT NULL, 
    content_id INTEGER NOT NULL, 
    comment_lv1_id BIGINT NOT NULL,
    comment_lv2_id BIGINT NOT NULL,
    owner_id BIGINT, 
    content_text VARCHAR(2000), 
    content_type CHAR(1), 
    content_uri VARCHAR(255), 
    block_at TIMESTAMP(6),
    block_cause_id INTEGER, 
    block_flag CHAR(1) NOT NULL, 
    create_at TIMESTAMP(6), 
    del_at TIMESTAMP(6), 
    del_flag CHAR(1) NOT NULL, 
    update_at TIMESTAMP(6), 
    PRIMARY KEY (page_id, status_id, content_id, comment_lv1_id, comment_lv2_id )
    );
CREATE TABLE m_comment_lv2_like_info (
    page_id BIGINT NOT NULL, 
    status_id BIGINT NOT NULL, 
    content_id INTEGER NOT NULL, 
    comment_lv1_id BIGINT NOT NULL, 
    comment_lv2_id BIGINT NOT NULL, 
    user_id BIGINT NOT NULL, 
    like_type CHAR(1), 
    create_at TIMESTAMP(6), 
    PRIMARY KEY (page_id, status_id, content_id, comment_lv1_id, comment_lv2_id, user_id )
    );

ALTER TABLE m_comment_lv1_info ADD CONSTRAINT m_comment_lv1_info_block_cause_info_fk 
    FOREIGN KEY ( block_cause_id) 
    REFERENCES m_block_cause_info ( block_cause_id);

ALTER TABLE m_comment_lv1_info ADD CONSTRAINT m_comment_lv1_info_owner_info_fk
    FOREIGN KEY( owner_id) 
    REFERENCES m_user_info( user_id);

ALTER TABLE m_comment_lv1_info ADD CONSTRAINT m_comment_lv1_info_status_content_info_fk 
    FOREIGN KEY ( page_id, status_id, content_id) 
    REFERENCES m_status_content_info ( page_id, status_id, content_id);

ALTER TABLE m_comment_lv1_like_info ADD CONSTRAINT m_comment_lv1_like_info_comment_lv1_info_fk 
    FOREIGN KEY ( page_id, status_id, content_id, comment_lv1_id) 
    REFERENCES m_comment_lv1_info ( page_id, status_id, content_id, comment_lv1_id);

ALTER TABLE m_comment_lv1_like_info ADD CONSTRAINT m_comment_lv1_like_info_user_info_fk 
    FOREIGN KEY (user_id) 
    REFERENCES m_user_info ( user_id);

ALTER TABLE m_comment_lv2_info ADD CONSTRAINT m_comment_lv2_info_block_cause_info_fk 
    FOREIGN KEY (block_cause_id) 
    REFERENCES m_block_cause_info ( block_cause_id);

ALTER TABLE m_comment_lv2_info ADD CONSTRAINT m_comment_lv2_info_owner_info_fk
    FOREIGN KEY( owner_id) 
    REFERENCES m_user_info( user_id);

ALTER TABLE m_comment_lv2_info ADD CONSTRAINT m_comment_lv1_info_comment_lv2_info_fk 
    FOREIGN KEY ( page_id, status_id, content_id, comment_lv1_id ) 
    REFERENCES m_comment_lv1_info ( page_id, status_id, content_id, comment_lv1_id);

ALTER TABLE m_comment_lv2_like_info ADD CONSTRAINT m_comment_lv2_like_info_comment_lv2_info_fk 
    FOREIGN KEY (page_id, status_id, content_id, comment_lv1_id, comment_lv2_id) 
    REFERENCES m_comment_lv2_info (page_id, status_id, content_id, comment_lv1_id, comment_lv2_id);

ALTER TABLE m_comment_lv2_like_info ADD CONSTRAINT m_comment_lv2_like_info_user_info_fk 
    FOREIGN KEY ( user_id) 
    REFERENCES m_user_info ( user_id);
