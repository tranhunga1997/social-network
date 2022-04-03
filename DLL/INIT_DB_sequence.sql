SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS m_sequence_info CASCADE;

CREATE TABLE m_sequence_info(
    sequence_id VARCHAR(20) PRIMARY KEY,
    current_num BIGINT NOT NULL,
    max_num BIGINT NOT NULL
);

INSERT INTO m_sequence_info(sequence_id, current_num, max_num) VALUES("USER_ID_SEQ", 0, 999999999999999999);
INSERT INTO m_sequence_info(sequence_id, current_num, max_num) VALUES("PAGE_ID_SEQ", 0, 999999999999999999);

INSERT INTO m_sequence_info(sequence_id, current_num, max_num) VALUES("TEST_ID_SEQ", 0, 999999999999999999);

