create table reimbursement_status (
id uuid default gen_random_uuid(),
"name" varchar unique,

primary key (id)
);


create table reimbursement_type (
id uuid default gen_random_uuid(),
"name" varchar unique,

primary key (id)
);



create table user_role (
id uuid default gen_random_uuid(),
"name" varchar unique,

primary key (id)
);




create table "user" (
id uuid default gen_random_uuid(),
username varchar not null unique,
email varchar not null unique,
"password" varchar not null,
first_name varchar not null,
last_name varchar not null,
is_active boolean,
role_id uuid,

foreign key (role_id) references user_role (id),
primary key (id)
);



create table reimbursement(
id uuid default gen_random_uuid(),
amount numeric(6,2) not null,
submitted timestamp default CURRENT_TIMESTAMP,
resolved timestamp ,
description varchar,
author_id uuid not null,
resolver_id uuid default null,
status_id uuid default '41301461-daf4-41d1-91e1-767eb87c398d',
type_id uuid default 'cadb6fcb-06ab-4583-b467-711185626cb7',

foreign key (author_id) references "user" (id),
foreign key (resolver_id) references "user" (id),
foreign key (status_id) references reimbursement_status (id),
foreign key (type_id) references reimbursement_type (id),
primary key (id)
);




