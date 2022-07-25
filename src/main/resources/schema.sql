create table if not exists image(
    id serial primary key,
    filename varchar(255) unique,
    mime_type varchar(30),
    data bytea
);