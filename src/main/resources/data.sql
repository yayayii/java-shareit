delete from comment;
delete from booking;
delete from item;
delete from request;
delete from user_account;

alter sequence comment_id_seq restart with 1;
alter sequence booking_id_seq restart with 1;
alter sequence item_id_seq restart with 1;
alter sequence request_id_seq restart with 1;
alter sequence user_account_id_seq restart with 1;