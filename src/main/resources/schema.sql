CREATE TABLE tasks (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(200)  NOT NULL,
    description  VARCHAR(1000),
    status       VARCHAR(20)   NOT NULL DEFAULT 'PENDING',
    created_at   TIMESTAMP     NOT NULL,
    updated_at   TIMESTAMP     NOT NULL
);