--liquibase formatted sql
--changeset roberto:202507261052
--comment: boards table create

CREATE TABLE boards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

--rollback DROP TABLE BOARDS