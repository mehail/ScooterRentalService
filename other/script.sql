DROP TABLE IF EXISTS promo_cods;
DROP TABLE IF EXISTS rental_sessions;
DROP TABLE IF EXISTS season_tickets;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS scooters;
DROP TABLE IF EXISTS scooter_types;
DROP TABLE IF EXISTS makers;
DROP TABLE IF EXISTS point_of_rentals;
DROP TABLE IF EXISTS addresses;
DROP TABLE IF EXISTS cities;
DROP TABLE IF EXISTS countries;

create table countries
(
    id      bigserial   not null
        constraint countries_pk
            primary key,
    country varchar(64) not null
);

alter table countries
    owner to postgres;

create unique index countries_country_uindex
    on countries (country);

create unique index countries_id_uindex
    on countries (id);

create table cities
(
    id         bigserial   not null
        constraint cities_pk
            primary key,
    city       varchar(64) not null,
    country_id bigint      not null
        constraint cities_countries_id_fk
            references countries
);

alter table cities
    owner to postgres;

create unique index cities_city_uindex
    on cities (city);

create unique index cities_id_uindex
    on cities (id);

create table point_of_rentals
(
    id        bigserial   not null
        constraint paint_of_rentals_pk
            primary key,
    name      varchar(64) not null,
    address   varchar(64) not null,
    cities_id bigint      not null
        constraint point_of_rentals_cities_id_fk
            references cities
);

alter table point_of_rentals
    owner to postgres;

create unique index paint_of_rentals_id_uindex
    on point_of_rentals (id);

create unique index paint_of_rentals_name_uindex
    on point_of_rentals (name);

create unique index point_of_rentals_address_uindex
    on point_of_rentals (address);

create table makers
(
    id    bigserial   not null
        constraint makers_pk
            primary key,
    maker varchar(64) not null
);

alter table makers
    owner to postgres;

create unique index makers_id_uindex
    on makers (id);

create unique index makers_maker_uindex
    on makers (maker);

create table scooter_types
(
    id               bigserial        not null
        constraint scooter_types_pk
            primary key,
    model            varchar(64)      not null,
    maker_id         bigint           not null
        constraint scooter_types_makers_id_fk
            references makers,
    max_speed        double precision not null,
    price_per_minute integer          not null
);

alter table scooter_types
    owner to postgres;

create unique index scooter_types_id_uindex
    on scooter_types (id);

create unique index scooter_types_model_uindex
    on scooter_types (model);

create table scooters
(
    serial_number      varchar(64) not null
        constraint scooters_pk
            primary key,
    point_of_rental_id bigint      not null
        constraint scooters_point_of_rentals_id_fk
            references point_of_rentals,
    scooter_type_id    bigint      not null
        constraint scooters_scooter_types_id_fk
            references scooter_types,
    time_of_use        integer     not null
);

alter table scooters
    owner to postgres;

create unique index scooters_serial_number_uindex
    on scooters (serial_number);

create table roles
(
    id   bigserial   not null
        constraint roles_pk
            primary key,
    name varchar(64) not null
);

alter table roles
    owner to postgres;

create unique index roles_id_uindex
    on roles (id);

create unique index roles_name_uindex
    on roles (name);

create table users
(
    id         bigserial   not null
        constraint users_pk
            primary key,
    role_id    bigint      not null
        constraint users_roles_id_fk
            references roles,
    login      varchar(64) not null,
    password   varchar(64) not null,
    first_name varchar(64) not null,
    last_name  varchar(64) not null,
    birthday   date        not null,
    email      varchar(64) not null,
    balance    integer     not null
);

alter table users
    owner to postgres;

create unique index users_email_uindex
    on users (email);

create unique index users_id_uindex
    on users (id);

create unique index users_login_uindex
    on users (login);

create table season_tickets
(
    id                bigserial not null
        constraint season_tickets_pk
            primary key,
    user_id           bigint    not null
        constraint season_tickets_users_id_fk
            references users,
    scooter_type_id   bigint    not null
        constraint season_tickets_scooter_types_id_fk
            references scooter_types,
    price             integer   not null,
    remaining_time    integer   not null,
    start_date        timestamp not null,
    expired_date      timestamp,
    available_for_use boolean   not null
);

alter table season_tickets
    owner to postgres;

create unique index season_tickets_id_uindex
    on season_tickets (id);

create table rental_sessions
(
    id                    bigserial   not null
        constraint sessions_pk
            primary key,
    user_id               bigint      not null
        constraint sessions_users_id_fk
            references users,
    begin                 timestamp   not null,
    "end"                 timestamp,
    scooter_serial_number varchar(64) not null
        constraint rental_sessions_scooters_serial_number_fk
            references scooters,
    rate                  integer     not null,
    season_ticket_id      bigint
        constraint sessions_season_tickets_id_fk
            references season_tickets
);

alter table rental_sessions
    owner to postgres;

create table promo_cods
(
    name                varchar(64) not null
        constraint promo_cods_pk
            primary key,
    start_date          timestamp,
    expired_date        timestamp,
    discount_percentage integer,
    bonus_point         integer,
    rental_session_id   bigint
        constraint promo_cods_sessions_id_fk
            references rental_sessions
);

alter table promo_cods
    owner to postgres;

create unique index promo_cods_name_uindex
    on promo_cods (name);

