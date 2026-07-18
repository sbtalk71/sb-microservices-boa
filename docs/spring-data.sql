create database springdb;
use springdb;

create table myemp (EMPNO integer 
not null, NAME varchar(15),
SALARY double,
ADDRESS varchar(15),
 primary key (EMPNO));

insert into myemp (EMPNO,NAME,SALARY,ADDRESS) values (100,'Shantanu',30000,'hyderabad');
insert into myemp (EMPNO,NAME,SALARY,ADDRESS) values (101,'Rajiv',30000,'hyderabad');
insert into myemp (EMPNO,NAME,SALARY,ADDRESS) values (102,'Shankar',30000,'bengaluru');
insert into myemp (EMPNO,NAME,SALARY,ADDRESS) values (103,'Madhu',30000,'hyderabad');
insert into myemp (EMPNO,NAME,SALARY,ADDRESS) values (104,'Kannan',30000,'Dhanbad');
insert into myemp (EMPNO,NAME,SALARY,ADDRESS) values (105,'Vimal',30000,'chennai');
insert into myemp (EMPNO,NAME,SALARY,ADDRESS) values (106,'Rupa',30000,'bhopal');
insert into myemp (EMPNO,NAME,SALARY,ADDRESS) values (107,'Pavan',30000,'hyderabad');
insert into myemp (EMPNO,NAME,SALARY,ADDRESS) values (108,'Kalyan',30000,'chennai');
insert into myemp (EMPNO,NAME,SALARY,ADDRESS) values (109,'Bhavesh',30000,'hyderabad');
insert into myemp (EMPNO,NAME,SALARY,ADDRESS) values (110,'Arun',30000,'pune');
insert into myemp (EMPNO,NAME,SALARY,ADDRESS) values (111,'Kanti',30000,'hyderabad');
insert into myemp (EMPNO,NAME,SALARY,ADDRESS) values (112,'Suraj',30000,'pune');
commit;