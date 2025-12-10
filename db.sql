create database StudentDB;
use StudentDB;

Create table students (
	id INT Auto_increment primary key,
    first_name varchar(50),
    last_name varchar(50),
    age int,
    email varchar(100)
);