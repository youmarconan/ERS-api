insert into reimbursement_status ("name") values ('pending'),('approved'),('denied');

insert into reimbursement_type ("name") values ('lodging'),('travel'),('food'),('other');

insert into user_role ("name") values ('admin'),('manager'),('employee');

select * from user_role;