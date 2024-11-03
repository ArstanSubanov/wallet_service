CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS wallet
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    balance    NUMERIC(15, 2) NOT NULL,
    created_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    version    BIGINT           DEFAULT 0
);