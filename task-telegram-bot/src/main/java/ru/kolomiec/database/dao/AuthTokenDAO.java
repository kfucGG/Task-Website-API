package ru.kolomiec.database.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionImpl;
import ru.kolomiec.database.HibernateConnection;
import ru.kolomiec.database.entity.AuthToken;
import ru.kolomiec.database.entity.Person;

public class AuthTokenDAO {

    private final SessionFactory sessionFactory;
    private PersonDAO personDAO = new PersonDAO();
    public AuthTokenDAO() {
        this.sessionFactory = HibernateConnection.getSessionFactory();
    }

}
