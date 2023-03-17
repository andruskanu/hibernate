package dao;

import model.Project;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Optional;

public class ProjectRepository {

    private SessionFactory sessionFactory;

    public ProjectRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<Project> findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Project project = session.find(Project.class, id);
            return Optional.ofNullable(project);
        }
    }

    public void save(Project project) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.persist(project);
                transaction.commit();
            } catch (Exception e) {
                System.out.println("I couldn't insert the project because: " + e);
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        }
    }

    public void delete(Project project) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.remove(project);
                transaction.commit();
            } catch (Exception e) {
                System.out.println("I couldn't insert the project because: " + e);
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        }
    }

    public void update(Project project) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.merge(project);
                transaction.commit();
            } catch (Exception e) {
                System.out.println("I couldn't insert the project because: " + e);
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        }
    }
}
