CREATE SCHEMA IF NOT EXISTS app_schema;

create table if not exists app_schema.entities
(
    id   uuid not null
        primary key,
    date timestamp(6) with time zone,
    name varchar(255)
);


INSERT INTO entities(id, name, date) VALUES ('adefec47-dee5-44a6-be03-5fad3052eeb4', 'testName1', '2100-03-03 00:00:00.123432 +00:00');
INSERT INTO entities(id, name, date) VALUES ('adefec47-dee5-44a6-be03-5fad3052eeb5', 'testName2', '2100-03-03 00:00:00.123432 +00:00');
INSERT INTO entities(id, name, date) VALUES ('adefec47-dee5-44a6-be03-5fad3052eeb6', 'testName3', '2100-03-03 00:00:00.123432 +00:00');