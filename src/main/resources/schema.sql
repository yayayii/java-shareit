create table if not exists user_account (
    id serial primary key,
    name varchar unique not null,
    email varchar unique not null
);

create table if not exists request (
    id serial primary key,
    description varchar,
    requestor_id int not null references user_account on delete cascade
);

create table if not exists item (
    id serial primary key,
    name varchar not null,
    description varchar not null,
    is_available bool default true,
    owner_id int not null references user_account on delete cascade
);

create table if not exists booking_status (
    id serial primary key,
    name varchar unique not null
);

create table if not exists booking (
    id serial primary key,
    start_date timestamp not null,
    end_date timestamp not null check (start_date < end_date),
    item_id int unique not null references item on delete cascade,
    booker_id int unique not null references user_account on delete cascade,
    status_id int not null references booking_status on delete restrict
);