-- DROP TABLE
SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS m_block_cause_info CASCADE;
DROP TABLE IF EXISTS m_page_role_info CASCADE;
DROP TABLE IF EXISTS m_page_info CASCADE;
DROP TABLE IF EXISTS m_page_follow_info CASCADE;
DROP TABLE IF EXISTS m_page_like_info CASCADE;
DROP TABLE IF EXISTS m_page_member_info CASCADE;
DROP TABLE IF EXISTS m_page_role_info CASCADE;
DROP VIEW IF EXISTS v_page_search_info;
-- CREATE TABLE

CREATE TABLE m_block_cause_info(
    block_cause_id INT PRIMARY KEY, 
    block_title VARCHAR(100) NOT NULL, 
    block_content VARCHAR(500) NOT NULL   
);


CREATE TABLE m_page_role_info(
    page_role_id BIGINT PRIMARY KEY,
    page_role_name VARCHAR(255)
);

CREATE TABLE m_page_info(
    page_id BIGINT PRIMARY KEY,
    owner_id BIGINT NOT NULL,
    page_name VARCHAR(200) NOT NULL, 
    page_type CHAR(1) NOT NULL,
    del_flag CHAR(1) NOT NULL,
    block_flag CHAR(1) NOT NULL,
    block_cause_id INT,
    create_at TIMESTAMP,
    update_at TIMESTAMP,
    del_at TIMESTAMP,
    block_at TIMESTAMP
);

CREATE TABLE m_page_follow_info(
    page_id BIGINT,
    user_id BIGINT,
    create_at TIMESTAMP,
    PRIMARY KEY(page_id, user_id)
);


CREATE TABLE m_page_like_info(
    page_id BIGINT,
    user_id BIGINT,
    create_at TIMESTAMP,
    PRIMARY KEY(page_id, user_id)
);

CREATE TABLE m_page_member_info(
    page_id BIGINT,
    member_id BIGINT,
    history_id INT,
    page_role_id BIGINT,
    is_accept CHAR NOT NULL,
    accept_at TIMESTAMP,
    create_at TIMESTAMP,
    update_at TIMESTAMP,
    PRIMARY KEY(page_id, member_id)
);

-- ADD FOREIGN KEY
ALTER TABLE m_page_info ADD CONSTRAINT page_block_cause_fk 
    FOREIGN KEY (block_cause_id) REFERENCES m_block_cause_info(block_cause_id);

ALTER TABLE m_page_follow_info ADD CONSTRAINT page_follow_page_fk
    FOREIGN KEY (page_id) REFERENCES m_page_info(page_id);

ALTER TABLE m_page_follow_info ADD CONSTRAINT page_follow_user_fk
    FOREIGN KEY (user_id) REFERENCES m_user_info(user_id);

ALTER TABLE m_page_like_info ADD CONSTRAINT page_like_page_fk
    FOREIGN KEY (page_id) REFERENCES m_page_info(page_id);

ALTER TABLE m_page_like_info ADD CONSTRAINT page_like_user_fk
    FOREIGN KEY (user_id) REFERENCES m_user_info(user_id);

ALTER TABLE m_page_member_info ADD CONSTRAINT page_member_page_fk
    FOREIGN KEY (page_id) REFERENCES m_page_info(page_id);

ALTER TABLE m_page_member_info ADD CONSTRAINT page_member_user_fk
    FOREIGN KEY (member_id) REFERENCES m_user_info(user_id);

ALTER TABLE m_page_member_info ADD CONSTRAINT page_member_page_role_fk
    FOREIGN KEY (page_role_id) REFERENCES m_page_role_info(page_role_id);

-- VIEW
CREATE VIEW v_page_search_info AS
SELECT 
    p.page_id,
    p.owner_id,
    p.page_name, 
    p.page_type ,
    p.del_flag ,
    p.block_flag  ,
    p.block_cause_id ,
    p.create_at ,
    p.update_at,
    p.del_at,
    p.block_at,
    (SELECT COUNT(*) FROM m_page_member_info m WHERE m.page_id = p.page_id) AS member_count,
    (SELECT COUNT(*) FROM m_page_follow_info f WHERE f.page_id = p.page_id) AS follow_count,
    (SELECT COUNT(*) FROM m_page_like_info l WHERE l.page_id = p.page_id) AS like_count,
    u.first_name AS owner_first_name,
    u.last_name AS owner_last_name
FROM 
    m_page_info p
JOIN
    m_user_info u ON p.owner_id = u.user_id;
