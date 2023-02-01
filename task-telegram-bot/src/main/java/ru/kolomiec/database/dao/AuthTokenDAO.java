package ru.kolomiec.database.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.kolomiec.database.HibernateConnection;
import ru.kolomiec.database.entity.AuthToken;
import ru.kolomiec.database.entity.Person;

public class AuthTokenDAO {

    private final SessionFactory sessionFactory;
    private PersonDAO personDAO = new PersonDAO();
    public AuthTokenDAO() {
        this.sessionFactory = HibernateConnection.getInstance().getSessionFactory();
    }

    public void saveAuthTokenToPersonByPersonChatId(AuthToken token, Long chatId) {
        Session session = sessionFactory.getCurrentSession();
        session.getTransaction().begin();
        Person person = personDAO.findPersonByChatId(chatId);
        token.setOwner(person);
        session.save(token);
        session.getTransaction().commit();
    }
}
