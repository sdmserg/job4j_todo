-- liquibase formatted sql

-- changeset sdmserg2021@gmail.com:001_ddL_create_tasks_table.sql
CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    created TIMESTAMP,
    done BOOLEAN
);

-- rollback DROP TABLE tasks;