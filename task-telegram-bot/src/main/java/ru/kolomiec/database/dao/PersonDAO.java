package ru.kolomiec.database.dao;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.kolomiec.database.HibernateConnection;
import ru.kolomiec.database.entity.AuthToken;
import ru.kolomiec.database.entity.Person;

import java.util.List;

@Slf4j
public class PersonDAO {

    private final SessionFactory sessionFactory;

    public PersonDAO() {
        this.sessionFactory = HibernateConnection.getSessionFactory();
    }

    public void savePerson(Person person) {
        Session session = null;
        try {
            session = sessionFactory.getCurrentSession();
            session.getTransaction().begin();
            session.save(person);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    public void savePerson(Person person, AuthToken authToken) {
        log.info(String.format(
                "Try to saving person with name: %s and token starts with %s",
                person.getUsername(), authToken.getToken().substring(0, 8)
        ));
        Session session = null;
        try {
            session = sessionFactory.getCurrentSession();
            session.getTransaction().begin();
            person.setAuthToken(authToken);
            authToken.setOwner(person);
            session.save(person);
            session.getTransaction().commit();
        } catch(Exception e) {
            log.info(String.format(
                    "saving person: %s is failure", person.getUsername()
            ));
            session.getTransaction().rollback();
        }
    }

    public void updatePerson(Person person, AuthToken authToken) {
        log.info(String.format("Updating person: %s", person.getUsername()));
        Session session = null;
        deletePersonTokenById(person.getId());
        try {
            session = sessionFactory.getCurrentSession();
            session.getTransaction().begin();
            authToken.setOwner(person);
            person.setAuthToken(authToken);
            session.update(person);
            session.getTransaction().commit();
        } catch(Exception e) {
            log.info(String.format("Updating person: %s is failure", person.getUsername()));
            session.getTransaction().rollback();
        }
    }
    public void deletePersonTokenById(Long id) {
        log.info("deleting person with id " + id);
        Session session = null;
        try {
            session = sessionFactory.getCurrentSession();
            session.getTransaction().begin();
            Query query = session.createNativeQuery("delete from auth_token where person_id = :id");
            query.setParameter("id", id);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            log.info(String.format(
                    "deleting person with id: %s is failure"
            ));
            session.getTransaction().rollback();
        }

    }
    public Person findPersonByChatId(Long chatId) {
        log.info(String.format(
                "finding person by chat id: %s", chatId
        ));
        Session session = null;
        Person personFromDb = null;
        try {
            session = sessionFactory.getCurrentSession();
            session.getTransaction().begin();
            Query<Person> query = session.createQuery("from Person p where p.chatId = :chatId");
            query.setParameter("chatId", chatId);
            personFromDb = query.uniqueResult();
            session.getTransaction().commit();
        } catch(Exception e) {
            log.info(String.format(
                    "Person with chat id : %s not found", chatId
            ));
            session.getTransaction().rollback();
        }
        return personFromDb;
    }
    public Person findPersonByUsername(String username) {
        log.info("finding person by username: " + username);
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.getTransaction().begin();
            Query<Person> query = session.createQuery("from Person p where p.username = :username");
            query.setParameter("username", username);
            List<Person> list = query.list();
            System.out.println(list);
            if (list.size() == 1)
                return list.get(0);
            session.getTransaction().commit();
            session.close();
        } catch(Exception e) {
            log.info(String.format(
                    "Person by username: %s not found", username
            ));
            session.getTransaction().rollback();
            session.close();
        }
        throw new RuntimeException("Person with such username not found");
    }

    public boolean isPersonSavedInDb(Long chatId) {
        Session session = null;
        Person personFromDb = null;
        try {
            session = sessionFactory.getCurrentSession();
            session.getTransaction().begin();
            Query<Person> query = session.createQuery("from Person p where p.chatId = :chatId", Person.class);
            query.setParameter("chatId", chatId);
            personFromDb = query.uniqueResult();
            session.getTransaction().commit();
        } catch(Exception e) {
            session.getTransaction().rollback();
        }
        return personFromDb != null;
    }
}
