create table reimbursement_status (
id varchar default gen_random_uuid (),
"name" varchar unique,

primary key (id)
);



create table reimbursement_type (
id varchar default gen_random_uuid (),
"name" varchar unique,

primary key (id)
);



create table user_role (
id varchar default gen_random_uuid (),
"name" varchar unique,

primary key (id)
);




create table "user" (
id varchar default gen_random_uuid (),
username varchar not null unique,
email varchar not null unique,
"password" varchar not null,
first_name varchar not null,
last_name varchar not null,
is_active boolean,
role_id varchar,

foreign key (role_id) references user_role (id),
primary key (id)
);




create table reimbursement (
id varchar default gen_random_uuid (),
amount int not null,
submitted timestamp not null,
resolved timestamp,
description varchar not null,
payment_id varchar,
author_id varchar not null,
resolver_id varchar,
status_id varchar not null,
type_id varchar not null,

foreign key (author_id) references "user" (id),
foreign key (resolver_id) references "user" (id),
foreign key (status_id) references reimbursement_status (id),
foreign key (type_id) references reimbursement_type (id),
primary key (id)
);
