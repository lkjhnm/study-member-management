CREATE TABLE G_USER
(
    id            VARCHAR primary key,
    email         VARCHAR NOT NULL,
    password      VARCHAR NOT NULL,
    nickname          VARCHAR NOT NULL,
    interest_tags VARCHAR,
    fcm_token      VARCHAR,
    constraint uc_user UNIQUE (email)
);
