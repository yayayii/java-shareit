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
    is_available bool not null,
    owner_id int not null references user_account on delete cascade,
    request_id int unique references request
);

create table if not exists booking (
    id serial primary key,
    start_date timestamp not null,
    end_date timestamp not null,
    status varchar not null,
    item_id int not null references item on delete cascade,
    booker_id int not null references user_account on delete cascade
);