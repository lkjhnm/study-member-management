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

CREATE TABLE AUTHENTICATION
(
    id            VARCHAR primary key,
    REFRESH_TOKEN VARCHAR NOT NULL,
    ACCESS_TOKEN VARCHAR NOT NULL,
    constraint uc_token UNIQUE (REFRESH_TOKEN, ACCESS_TOKEN)
)
