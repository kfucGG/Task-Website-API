package ru.kolomiec.database.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.kolomiec.database.HibernateConnection;
import ru.kolomiec.database.entity.AuthToken;
import ru.kolomiec.database.entity.Person;

import java.util.List;


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
        Session session = null;
        try {
            session = sessionFactory.getCurrentSession();
            session.getTransaction().begin();
            person.setAuthToken(authToken);
            authToken.setOwner(person);
            session.save(person);
            session.getTransaction().commit();
        } catch(Exception e) {
            session.getTransaction().rollback();
        }
    }

    public void updatePerson(Person person, AuthToken authToken) {
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
            session.getTransaction().rollback();
        }
    }
    public void deletePersonTokenById(Long id) {
        Session session = null;
        try {
            session = sessionFactory.getCurrentSession();
            session.getTransaction().begin();
            Query query = session.createNativeQuery("delete from auth_token where person_id = :id");
            query.setParameter("id", id);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }

    }
    public Person findPersonByChatId(Long chatId) {
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
            session.getTransaction().rollback();
        }
        return personFromDb;
    }
    public Person findPersonByUsername(String username) {
        Session session = null;
        try {
            session = sessionFactory.getCurrentSession();
            session.getTransaction().begin();
            Query<Person> query = session.createQuery("from Person p where p.username = :username");
            query.setParameter("username", username);
            List<Person> list = query.list();
            System.out.println(list);
            if (list.size() == 1)
                return list.get(0);
            session.getTransaction().commit();
        } catch(Exception e) {
            session.getTransaction().rollback();
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
