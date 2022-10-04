insert into reimbursement_status ("name") values ('pending'),('approved'),('denied');

delete from reimbursement_status where id = '35b056a8-88b2-4a15-83b5-2975260b0d3e'
select * from reimbursement_status;

insert into reimbursement_type ("name") values ('lodging'),('travel'),('food'),('other');

select * from reimbursement_type;

insert into user_role ("name") values ('admin'),('manager'),('employee');

select * from user_role;

delete from user_role where id = 'e0ac0158-2b16-49fd-baa1-30a4b734eb35';

select "user".username as username,
"user".id  as user_id,
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


select reimbursement.id as reimbursement_id,
reimbursement.amount as amount,
reimbursement.submitted as submitted,
reimbursement.resolved as resolved,
reimbursement.description as description,
reimbursement.author_id as author_id,
reimbursement.resolver_id as resolver_id,
reimbursement_type."name" as "type",
reimbursement_status."name" as status
from reimbursement
join reimbursement_type
on reimbursement_type.id = reimbursement.type_id 
join reimbursement_status
on reimbursement_status.id = reimbursement.status_id ;

select * from "user" ;


select * from reimbursement ;

delete from reimbursement where id = '6a9890bd-26d7-4779-b469-64ce4707b813' ;



