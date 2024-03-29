CREATE TABLE task
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    task_name VARCHAR(255)                            NOT NULL,
    person_id BIGINT                                  NOT NULL,
    task_time time WITHOUT TIME ZONE,
    task_date date,
    CONSTRAINT pk_task PRIMARY KEY (id)
);

ALTER TABLE task
    ADD CONSTRAINT FK_TASK_ON_PERSON FOREIGN KEY (person_id) REFERENCES person (id);