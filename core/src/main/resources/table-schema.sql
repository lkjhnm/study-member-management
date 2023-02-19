CREATE TABLE G_USER
(
    id            VARCHAR primary key,
    user_id       VARCHAR NOT NULL,
    email         VARCHAR NOT NULL,
    password      VARCHAR NOT NULL,
    nickname      VARCHAR NOT NULL,
    interest_tags VARCHAR,
    fcm_token     VARCHAR,
    constraint uc_user_id UNIQUE (user_id),
    constraint uc_email UNIQUE (email)
);

CREATE TABLE AUTHENTICATION
(
    id            VARCHAR primary key,
    REFRESH_TOKEN VARCHAR   NOT NULL,
    ACCESS_TOKEN  VARCHAR   NOT NULL,
    EXPIRED_AT    TIMESTAMP NOT NULL,
    constraint uc_token UNIQUE (REFRESH_TOKEN, ACCESS_TOKEN)
)
