delete from comment;
delete from booking;
delete from item;
delete from request;
delete from user_account;

alter table comment
    alter column id
        restart with 1;
alter table booking
    alter column id
        restart with 1;
alter table item
    alter column id
        restart with 1;
alter table request
    alter column id
        restart with 1;
alter table user_account
    alter column id
        restart with 1;