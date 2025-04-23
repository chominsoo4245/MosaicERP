
CREATE TABLE tb_access_log
(
    id               bigserial PRIMARY KEY,
    service_name     VARCHAR(100) NOT NULL,
    method           VARCHAR(10)  NOT NULL,
    path             TEXT         NOT NULL,
    ip               VARCHAR(45)  NOT NULL,
    user_agent       TEXT,
    status_code      INTEGER      NOT NULL,
    response_time_ms BIGINT       NOT NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tb_audit_log
(
    id          bigserial PRIMARY KEY,
    domain      VARCHAR(50) NOT NULL, -- 'auth', 'login', 'inventory' 등
    login_id    VARCHAR(100),
    action      VARCHAR(50) NOT NULL,
    description TEXT,
    ip          VARCHAR(45) NOT NULL,
    user_agent  TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);