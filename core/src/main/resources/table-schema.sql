CREATE TABLE MEMBER
(
    id            BIGINT primary key,
    user_id       VARCHAR,
    password      VARCHAR,
    name          VARCHAR,
    interest_tags VARCHAR,
    email         VARCHAR,
    fcm_token      VARCHAR
);
