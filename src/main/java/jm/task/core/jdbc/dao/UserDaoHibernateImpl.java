package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static  final SessionFactory sessionFactory = Util.getSessionFactory();
    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            String sql = "CREATE TABLE  IF NOT EXISTS user" +
                    "(id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL," +
                    "name VARCHAR (64)," +
                    "lastname VARCHAR (64)," +
                    "age TINYINT )";
            Query query = session.createSQLQuery(sql).addEntity(User.class);
            query.executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            System.out.println("Ошибка создания таблицы");
        }

    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            String sql = "DROP TABLE IF EXISTS user";
            Query query = session.createSQLQuery(sql)
                    .addEntity(User.class);
            query.executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            System.out.println("Таблица удалена");
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = sessionFactory.openSession();
        try (session) {
            session.beginTransaction();
            User user = new User(name, lastName, age);
            session.persist(user);
            session.getTransaction().commit();
            System.out.println("Добавлен пользователь " + user.getName() + " " + user.getLastName());
        } catch (HibernateException e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        }

    }

    @Override
    public void removeUserById(long id) {
        Session session = sessionFactory.openSession();
        try (session) {
            session.beginTransaction();
            session.remove(session.get(User.class, id));
            session.getTransaction().commit();
        } catch (HibernateException e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        }

    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User", User.class).list();
        }
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Удаление из User ошибка");
        }
    }
}
