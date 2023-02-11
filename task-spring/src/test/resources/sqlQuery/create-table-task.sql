CREATE TABLE IF NOT EXISTS task(
    id SERIAL PRIMARY KEY,
    task_name varchar(250) NOT NULL,
    person_id BIGINT REFERENCES Person(id),
    to_do_time timestamp
);