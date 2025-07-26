--liquibase formatted sql
--changeset roberto:202507261113
--comment: blocks table create

CREATE TABLE blocks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    blocked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    block_cause VARCHAR(255) NOT NULL,
    unblocked_at TIMESTAMP NULL,
    unblock_cause VARCHAR(255) NULL,
    card_id BIGINT NOT NULL,
    CONSTRAINT cards__blocks_fk FOREIGN KEY (card_id) REFERENCES cards(id) ON DELETE CASCADE
) ENGINE=InnoDB;

--rollback DROP TABLE blocks