delete from booking;
delete from booking_status;
delete from item;
delete from request;
delete from user_account;

alter sequence booking_id_seq restart with 1;
alter sequence booking_status_id_seq restart with 1;
alter sequence item_id_seq restart with 1;
alter sequence request_id_seq restart with 1;
alter sequence user_account_id_seq restart with 1;

insert into booking_status (name)
values ('WAITING'),
       ('APPROVED'),
       ('REJECTED'),
       ('CANCELED');