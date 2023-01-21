package ru.kolomiec.taskspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kolomiec.taskspring.entity.Task;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("from Task t where t.owner.id = :id")
    Optional<List<Task>> findAllByOwnerId(Long id);
}
