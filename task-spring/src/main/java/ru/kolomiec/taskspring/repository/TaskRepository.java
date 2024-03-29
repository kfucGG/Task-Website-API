package ru.kolomiec.taskspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kolomiec.taskspring.entity.Person;
import ru.kolomiec.taskspring.entity.Task;
import ru.kolomiec.taskspring.entity.ToDoTime;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("from Task t where t.owner.id = :id")
    Optional<List<Task>> findAllByOwnerId(Long id);

    @Query("from Task t where t.owner.username = :username")
    Optional<List<Task>> findAllByOwnerUsername(String username);

    @Query("from Task t where t.taskName = :taskName")
    Optional<Task> findTaskByTaskName(String taskName);

    List<Task> findTasksByTaskNameAndToDoTimeAndOwner(String taskName, ToDoTime toDoTime, Person taskOwner);

    @Query("from Task t where t.toDoTime.taskDate = current_date AND t.toDoTime.taskTime = :currentTime")
    Optional<List<Task>> findAllTasksWhichToDoTimeIsCurrentDateAndTime(@Param("currentTime") LocalTime currentTime);

}
