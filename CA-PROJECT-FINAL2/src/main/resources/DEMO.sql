
CREATE SCHEMA `ca_project2` ;




use ca_project2;
 drop table if exists leave_history_details;
 drop table if exists employee;

 drop table if exists leave_type;
 drop table if exists public_hollyday;
 drop table if exists role;
 create table employee (emp_id integer not null auto_increment, annual_leave_count integer not null, compensation_leave_count integer not null, email varchar(255) not null, full_name varchar(255) not null, medical_leave_count integer not null, password varchar(255) not null, user_name varchar(100) not null, reports_to integer, role_id integer, primary key (emp_id));
 create table leave_history_details (leave_history_id integer not null auto_increment, applying_reason varchar(255) not null, end_date date not null, rejection_reason varchar(255), start_date date not null, status varchar(255) not null, work_desemination varchar(255), emp_id integer, leave_type_id integer, primary key (leave_history_id)) ;
 create table leave_type (leave_type_id integer not null auto_increment, type varchar(255), primary key (leave_type_id));
 create table public_hollyday (hollyday_id integer not null auto_increment, description varchar(255), hollyday_name varchar(255) not null, start_date date not null, primary key (hollyday_id));
 create table role (role_id integer not null auto_increment, role_name varchar(100), primary key (role_id));
 alter table employee add constraint UK_o885fqgb71dmn4hp0p6rs4ms4 unique (user_name);
 alter table leave_type add constraint UK_61qanpjyq3t0h6i1xaqbb64b unique (type);
 alter table role add constraint UK_iubw515ff0ugtm28p8g3myt0h unique (role_name);
 alter table employee add constraint FKghecv11ypswk5w7mmcof2dscg foreign key (reports_to) references employee (emp_id) on delete no action;
 alter table employee add constraint FK3046kvjyysq288vy3lsbtc9nw foreign key (role_id) references role (role_id) on delete no action;
 alter table leave_history_details add constraint FKqmac42on7ad2s2v2cqc57fnql foreign key (emp_id) references employee (emp_id) on delete cascade;
 alter table leave_history_details add constraint FKh4li8wnuqa9v5hvmculqb53pp foreign key (leave_type_id) references leave_type (leave_type_id) on delete cascade;