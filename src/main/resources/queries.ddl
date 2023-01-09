create table users (
    id int primary key not null unique ,
    username varchar (50)
);

create schema public;

alter schema telegram owner to postgres;