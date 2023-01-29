package ru.kolomiec.taskspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kolomiec.taskspring.entity.Person;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("SELECT DISTINCT p from Person p left join fetch p.userTask where username = :username")
    Optional<Person> findByUsername(String username);

}
