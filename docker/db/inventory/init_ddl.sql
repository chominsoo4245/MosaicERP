create table tb_inventory
(
    inventory_id      serial
        primary key,
    item_id           integer                             not null,
    warehouse_id      integer                             not null,
    bin_id            integer,
    current_quantity  integer                             not null,
    reserved_quantity integer   default 0                 not null,
    version           integer   default 1                 not null,
    created_at        timestamp default CURRENT_TIMESTAMP not null,
    updated_at        timestamp default CURRENT_TIMESTAMP not null,
    lot_id            integer
);

create table tb_inventory_history
(
    transaction_id   serial
        primary key,
    item_id          integer                             not null,
    warehouse_id     integer                             not null,
    bin_id           integer,
    transaction_type varchar(20)                         not null,
    quantity_change  integer                             not null,
    pre_quantity     integer,
    post_quantity    integer,
    transaction_date timestamp default CURRENT_TIMESTAMP not null,
    origin_ref       varchar(100),
    operator         varchar(100),
    created_at       timestamp default CURRENT_TIMESTAMP not null,
    updated_at       timestamp default CURRENT_TIMESTAMP not null,
    lot_id           integer
);


create table public.tb_inbound_receipt
(
    receipt_id     serial
        primary key,
    supplier_id    integer                             not null,
    receipt_date   timestamp default CURRENT_TIMESTAMP not null,
    total_quantity integer                             not null,
    remarks        text,
    created_at     timestamp default CURRENT_TIMESTAMP not null,
    updated_at     timestamp default CURRENT_TIMESTAMP not null
);


create table public.tb_inbound_receipt_detail
(
    detail_id       serial
        primary key,
    receipt_id      integer                             not null,
    item_id         integer                             not null,
    lot_number      varchar(50),
    location_info   varchar(100),
    expiration_date date,
    quantity        integer                             not null,
    unit_cost       numeric(10, 2)                      not null,
    total_cost      numeric(10, 2)                      not null,
    created_at      timestamp default CURRENT_TIMESTAMP not null,
    updated_at      timestamp default CURRENT_TIMESTAMP not null
);

create table tb_outbound_receipt
(
    receipt_id     serial
        primary key,
    customer_id    integer                             not null,
    shipment_date  timestamp default CURRENT_TIMESTAMP not null,
    total_quantity integer                             not null,
    remarks        text,
    created_at     timestamp default CURRENT_TIMESTAMP not null,
    updated_at     timestamp default CURRENT_TIMESTAMP not null
);

create table tb_outbound_receipt_detail
(
    detail_id       serial
        primary key,
    receipt_id      integer                             not null,
    item_id         integer                             not null,
    lot_number      varchar(50),
    location_info   varchar(100),
    expiration_date date,
    quantity        integer                             not null,
    unit_price      numeric(10, 2)                      not null,
    total_price     numeric(10, 2)                      not null,
    created_at      timestamp default CURRENT_TIMESTAMP not null,
    updated_at      timestamp default CURRENT_TIMESTAMP not null
);

create table tb_lot
(
    lot_id          serial primary key,
    item_id         integer                             not null,
    lot_number      varchar(50)                         not null,
    initial_stock   integer   default 0                 not null,
    expiration_date date,
    location_info   varchar(100),
    created_at      timestamp default CURRENT_TIMESTAMP not null,
    updated_at      timestamp default CURRENT_TIMESTAMP not null
);

create table tb_serial
(
    serial_id     serial primary key,
    item_id       integer                             not null,
    lot_id        integer,
    serial_number varchar(50)                         not null,
    created_at    timestamp default CURRENT_TIMESTAMP not null,
    updated_at    timestamp default CURRENT_TIMESTAMP not null
);

create table tb_bin
(
    bin_id       serial
        primary key,
    warehouse_id integer                             not null,
    name         varchar(50)                         not null,
    description  varchar(200),
    created_at   timestamp default CURRENT_TIMESTAMP not null,
    updated_at   timestamp default CURRENT_TIMESTAMP not null
);

