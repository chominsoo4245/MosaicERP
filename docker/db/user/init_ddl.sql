create table tb_user
(
    id            bigserial primary key,
    login_id      varchar(50) not null unique,
    password      varchar,
    email         varchar(100),
    status_code   varchar(20) not null,  -- FK 제거, 논리적으로만 참조
    created_at    timestamp default now(),
    update_at     timestamp default now(),
    last_login_at timestamp,
    role_id       bigint      -- FK 제거, 논리적으로만 참조
);

create table tb_user_details
(
    user_id    bigint primary key,  -- FK 제거, 논리적으로만 참조
    name       varchar(100),
    birth_date date,
    gender     char,
    phone_num  varchar(20)
);

create table tb_auth_role
(
    id          bigserial primary key,
    name        varchar(20) not null unique,
    description text
);

create table tb_user_status
(
    status_code varchar(20) not null
        constraint status_code_pk
            primary key,
    description varchar(255)
);
