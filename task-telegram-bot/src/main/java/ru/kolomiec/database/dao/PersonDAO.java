package ru.kolomiec.database.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
        Session session = sessionFactory.getCurrentSession();
        session.getTransaction().begin();
        session.save(person);
        session.getTransaction().commit();
    }

    public void savePerson(Person person, AuthToken authToken) {
        Session session = sessionFactory.getCurrentSession();
        session.getTransaction().begin();
        person.setAuthToken(authToken);
        authToken.setOwner(person);
        session.save(person);
        session.getTransaction().commit();
    }

    public void updatePerson(Person person, AuthToken authToken) {
        Session session = sessionFactory.getCurrentSession();
        session.getTransaction().begin();
        deletePersonTokenById(person.getId());
        authToken.setOwner(person);
        person.setAuthToken(authToken);
        session.update(person);
        session.getTransaction().commit();
    }
    public void deletePersonTokenById(Long id) {
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Query query = session.createNativeQuery("delete from auth_token where person_id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
        session.getTransaction().commit();
    }
    public Person findPersonByChatId(Long chatId) {
        Session session = sessionFactory.getCurrentSession();
        session.getTransaction().begin();
        Query<Person> query = session.createQuery("from Person p where p.chatId = :chatId");
        query.setParameter("chatId", chatId);
        List<Person> person = query.list();
        session.getTransaction().commit();
        return person.get(0);
    }

    public boolean isPersonSavedInDb(Long chatId) {
        Session session = sessionFactory.getCurrentSession();
        session.getTransaction().begin();
        Query<Person> query = session.createQuery("from Person p where p.chatId = :chatId", Person.class);
        query.setParameter("chatId", chatId);
        List<Person> list = query.list();
        session.getTransaction().commit();
        return !list.isEmpty();
    }
}
