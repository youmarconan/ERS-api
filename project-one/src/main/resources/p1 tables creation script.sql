create table reimbursement_status (
id serial,
"name" varchar unique,

primary key (id)
);


create table reimbursement_type (
id serial,
"name" varchar unique,

primary key (id)
);



create table user_role (
id serial,
"name" varchar unique,

primary key (id)
);




create table "user" (
id serial,
username varchar not null unique,
email varchar not null unique,
"password" varchar not null,
first_name varchar not null,
last_name varchar not null,
is_active boolean,
role_id serial,

foreign key (role_id) references user_role (id),
primary key (id)
);



create table reimbursement(
id serial,
amount numeric(6,2) not null,
submitted timestamp default CURRENT_TIMESTAMP,
resolved timestamp ,
description varchar,
author_id serial not null,
resolver_id int default null,
status_id int default 1,
type_id int default 1, 

foreign key (author_id) references "user" (id),
foreign key (resolver_id) references "user" (id),
foreign key (status_id) references reimbursement_status (id),
foreign key (type_id) references reimbursement_type (id),
primary key (id)
);

drop table reimbursement;


