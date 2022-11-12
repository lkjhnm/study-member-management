CREATE TABLE G_USER
(
    id            VARCHAR primary key,
    user_id       VARCHAR unique,
    password      VARCHAR,
    name          VARCHAR,
    interest_tags VARCHAR,
    email         VARCHAR,
    fcm_token      VARCHAR
);
