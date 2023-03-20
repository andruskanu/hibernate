package dao;

import model.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class EmployeeRepository {

    private SessionFactory sessionFactory;

    public EmployeeRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<Employee> findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Employee employee = session.find(Employee.class, id);
            return Optional.ofNullable(employee);
        }
    }

    public void save(Employee employee) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.persist(employee);
                transaction.commit();
            } catch (Exception e) {
                System.out.println("I couldn't insert the employee because: " + e);
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        }
    }

    public void delete(Employee employee) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.remove(employee);
                transaction.commit();
            } catch (Exception e) {
                System.out.println("I couldn't delete the employee because: " + e);
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        }
    }

    public void update(Employee employee) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.merge(employee);
                transaction.commit();
            } catch (Exception e) {
                System.out.println("I couldn't update the employee because: " + e);
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        }
    }

    public List<Employee> getAllEmployees() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Employee", Employee.class).list();
        }
    }
}
