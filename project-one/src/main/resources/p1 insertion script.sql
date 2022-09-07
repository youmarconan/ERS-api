insert into reimbursement_status ("name") values ('pending'),('approved'),('denied');

insert into reimbursement_type ("name") values ('lodging'),('travel'),('food'),('other');

insert into user_role ("name") values ('admin'),('manager'),('employee');

select * from user_role;


insert into "user" (username,email,"password",first_name,last_name,is_active,role_id) 
values ('youmarco','marcoyounan@gmail','p@$$word','marco','younan',true,'1');

select * from "user";

select "user".username as username,
"user".email as email,
"user"."password" as "password",
"user".first_name as first_name,
"user".last_name as last_name,
"user".is_active as is_active,
user_role.id as role_id,
user_role."name" as role_name
from "user" 
join user_role
on user_role.id = "user".role_id;




select "user".username as username, "user".email as email, "user"."password" as "password", "user".first_name as first_name, "user".last_name as last_name, "user".is_active as is_active, user_role.id as role_id, user_role.name as role_name from "user" join user_role on user_role.id = "user".role_id where username = 'youmarco' and "password" = 'p@$$word' ;
