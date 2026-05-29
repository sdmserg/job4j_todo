-- liquibase formatted sql

-- changeset sdmserg2021@gmail.com:002_ddl_create_todo_users_table.sql
ALTER TABLE tasks
ADD COLUMN user_id INT REFERENCES todo_users(id);

-- rollback ALTER TABLE tasks DROP COLUMN user_id;