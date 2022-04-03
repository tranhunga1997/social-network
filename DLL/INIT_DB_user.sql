-- DROP TABLE
SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS m_user_info CASCADE;
DROP TABLE IF EXISTS m_authenticate_info CASCADE;
DROP TABLE IF EXISTS t_login_token_info CASCADE;
DROP TABLE IF EXISTS t_forget_password_token_info CASCADE;
DROP TABLE IF EXISTS t_regist_token_info CASCADE;
DROP TABLE IF EXISTS h_login_info CASCADE;
DROP TABLE IF EXISTS t_regist_token_history;
DROP TABLE IF EXISTS m_permission_info CASCADE;
DROP TABLE IF EXISTS m_role_info CASCADE;
DROP TABLE IF EXISTS m_user_role_link CASCADE;
DROP TABLE IF EXISTS m_role_permission_link CASCADE;
-- CREATE TABLE


CREATE TABLE m_user_info(
    user_id BIGINT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    last_name VARCHAR(50),
    first_name VARCHAR(50),
    email VARCHAR(255) NOT NULL,
    role_id INT NOT NULL,
    is_enabled CHAR(1) NOT NULL,
    is_blocked CHAR(1) NOT NULL,
    create_at TIMESTAMP,
    update_at TIMESTAMP,
    del_at TIMESTAMP
);



CREATE TABLE m_authenticate_info(
    user_id BIGINT,
    history_id INT,
    password VARCHAR(255),
    login_failed_counter INT NOT NULL,
    create_at TIMESTAMP,
    update_at TIMESTAMP,
    del_at TIMESTAMP,
    PRIMARY KEY(user_id, history_id)
);


CREATE TABLE t_login_token_info(
    token VARCHAR(255) PRIMARY KEY,
    ip_address VARCHAR(15) NOT NULL,
    user_id BIGINT NOT NULL,
    token_expired_at TIMESTAMP,
    create_at TIMESTAMP,
    update_at TIMESTAMP,
    del_at TIMESTAMP
);

CREATE TABLE t_forget_password_token_info(
    token VARCHAR(255) PRIMARY KEY, 
    user_id BIGINT NOT NULL,
    token_expired_at TIMESTAMP,
    create_at TIMESTAMP,
    update_at TIMESTAMP,
    del_at TIMESTAMP
);


CREATE TABLE t_regist_token_info(
    token VARCHAR(255) PRIMARY KEY, 
    user_id BIGINT NOT NULL,
    token_expired_at TIMESTAMP,
    create_at TIMESTAMP,
    update_at TIMESTAMP,
    del_at TIMESTAMP
);

CREATE TABLE t_regist_token_history(
    token VARCHAR(255) PRIMARY KEY, 
    user_id BIGINT NOT NULL,
    token_expired_at TIMESTAMP,
    active_at TIMESTAMP,
    create_at TIMESTAMP,
    update_at TIMESTAMP,
    del_at TIMESTAMP
);

CREATE TABLE h_login_info(
    ip_address VARCHAR(15),
    user_id BIGINT NOT NULL,
    access_at TIMESTAMP,
    create_at TIMESTAMP,
    update_at TIMESTAMP,
    del_at TIMESTAMP,
    PRIMARY KEY(ip_address, access_at)
);


CREATE TABLE m_permission_info(
    permission_id INT PRIMARY KEY,
    permission_slug VARCHAR(32),
    permission_name VARCHAR(32),
    create_at TIMESTAMP,
    update_at TIMESTAMP,
    del_at TIMESTAMP
);


CREATE TABLE m_role_info(
    role_id INT PRIMARY KEY,
    role_slug VARCHAR(32),
    role_name VARCHAR(32),
    create_at TIMESTAMP,
    update_at TIMESTAMP,
    del_at TIMESTAMP
);


CREATE TABLE m_user_role_link(
    user_id BIGINT,
    role_id INT,
    PRIMARY KEY(user_id, role_id)
);

CREATE TABLE m_role_permission_link(
    role_id INT,
    permission_id INT,
    PRIMARY KEY(role_id, permission_id)
);

-- ADD FOREIGN KEY
ALTER TABLE m_authenticate_info ADD CONSTRAINT m_authenticate_user_fk
    FOREIGN KEY(user_id) REFERENCES m_user_info (user_id);

ALTER TABLE t_login_token_info ADD CONSTRAINT t_login_token_user_fk
    FOREIGN KEY(user_id) REFERENCES m_user_info (user_id);

ALTER TABLE t_forget_password_token_info ADD CONSTRAINT t_forget_password_user_fk
    FOREIGN KEY(user_id) REFERENCES m_user_info (user_id);

ALTER TABLE t_regist_token_info ADD CONSTRAINT t_regist_token_user_fk
    FOREIGN KEY(user_id) REFERENCES m_user_info (user_id);

ALTER TABLE t_regist_token_history ADD CONSTRAINT t_regist_token_history_user_fk
    FOREIGN KEY(user_id) REFERENCES m_user_info (user_id);

ALTER TABLE h_login_info ADD CONSTRAINT h_login_user_fk
    FOREIGN KEY(user_id) REFERENCES m_user_info (user_id);

ALTER TABLE m_user_role_link ADD CONSTRAINT m_user_role_link_user_fk
    FOREIGN KEY(user_id) REFERENCES m_user_info (user_id);

ALTER TABLE m_user_role_link ADD CONSTRAINT m_user_role_link_role_fk
    FOREIGN KEY(role_id) REFERENCES m_role_info (role_id);

ALTER TABLE m_role_permission_link ADD CONSTRAINT m_role_permission_link_role_fk
    FOREIGN KEY(role_id) REFERENCES m_role_info (role_id);

ALTER TABLE m_role_permission_link ADD CONSTRAINT m_role_permission_link_permission_fk
    FOREIGN KEY(permission_id) REFERENCES m_permission_info (permission_id);