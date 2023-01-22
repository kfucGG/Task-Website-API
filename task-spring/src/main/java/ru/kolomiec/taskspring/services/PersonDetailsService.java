package ru.kolomiec.taskspring.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolomiec.taskspring.entity.Person;
import ru.kolomiec.taskspring.entity.PersonDetailsSecurityEntity;
import ru.kolomiec.taskspring.repository.PersonRepository;
import ru.kolomiec.taskspring.services.interfaces.PersonService;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonDetailsService implements UserDetailsService {

    @PersistenceContext
    private EntityManager entityManager;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //todo fixed bug but need to normal
        Query query = entityManager.createQuery("from Person p where p.username = :username");
        Person person = (Person) query.setParameter("username", username).getSingleResult();
        return new PersonDetailsSecurityEntity(person);
    }
}
