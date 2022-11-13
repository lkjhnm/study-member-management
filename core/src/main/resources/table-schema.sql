CREATE TABLE G_USER
(
    id            VARCHAR primary key,
    user_id       VARCHAR NOT NULL ,
    password      VARCHAR NOT NULL,
    name          VARCHAR NOT NULL,
    email         VARCHAR NOT NULL,
    interest_tags VARCHAR,
    fcm_token      VARCHAR,
    constraint uc_user UNIQUE (user_id)
);
