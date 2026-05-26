-- liquibase formatted sql

-- changeset sdmserg2021@gmail.com:002_ddl_create_todo_users_table.sql
CREATE TABLE todo_users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    login VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL
);

-- rollback DROP TABLE todo_users;