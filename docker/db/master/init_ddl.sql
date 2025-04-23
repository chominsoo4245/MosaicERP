create table tb_item
(
    item_id             serial primary key,
    category_id         integer                             not null,
    code                varchar(50)                         not null,
    name                varchar(200)                        not null,
    description         text,
    unit                varchar(20),
    cost                numeric(10, 2),
    price               numeric(10, 2),
    is_lot_tracked      boolean   default false             not null,
    default_supplier_id integer,
    created_at          timestamp default CURRENT_TIMESTAMP not null,
    updated_at          timestamp default CURRENT_TIMESTAMP not null
);

create table tb_category
(
    category_id        serial,
    parent_category_id integer,
    category_type      varchar(50),
    category_code      varchar(20),
    name               varchar(100)      not null,
    description        text,
    level              integer default 1 not null,
    created_at         timestamp         not null,
    updated_at         timestamp         not null,
    short_code         varchar(10)       not null
);

create table tb_supplier
(
    supplier_id     serial primary key,
    name            varchar(200)                        not null,
    contact_details varchar(200),
    address         text,
    created_at      timestamp default CURRENT_TIMESTAMP not null,
    updated_at      timestamp default CURRENT_TIMESTAMP not null
);

create table tb_warehouse
(
    warehouse_id  serial primary key,
    name          varchar(100)                        not null,
    address_line1 varchar(200)                        not null,
    address_line2 varchar(200),
    city          varchar(100)                        not null,
    state         varchar(100),
    zipcode       varchar(20),
    country       varchar(100)                        not null,
    capacity      integer,
    created_at    timestamp default CURRENT_TIMESTAMP not null,
    updated_at    timestamp default CURRENT_TIMESTAMP not null
);



