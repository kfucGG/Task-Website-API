CREATE TABLE task (
    id SERIAL PRIMARY KEY,
    task_name varchar NOT NULL,
    person_id INT REFERENCES person(id)
);