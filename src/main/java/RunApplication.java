import config.HibernateUtils;
import dao.ProjectRepository;
import model.Employee;
import model.Project;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Optional;

public class RunApplication {

    public static void main(String[] args) {

        try (SessionFactory sessionFactory = HibernateUtils.getSessionFactory()) {
            insertSomeEmployees(sessionFactory);
            findEmployee(sessionFactory, 1);
            ProjectRepository projectRepository = new ProjectRepository(sessionFactory);

            projectRepository.save(new Project("Web Development"));
            projectRepository.save(new Project("Software Development"));
            projectRepository.save(new Project("R&D"));

            Optional<Project> byId = projectRepository.findById(3);
            if (byId.isPresent()) {
                Project project = byId.get();
                System.out.println("I found the project: " + project);
                project.setProjectName("Research & Development");
                projectRepository.update(project);
                System.out.println("I modified the project: " + project);
            } else {
                System.out.println("I have not found the project with id: 1");
            }
        }
    }

    private static void findEmployee(SessionFactory sessionFactory, long id) {
        try (Session session = sessionFactory.openSession()) {
            Employee employee = session.find(Employee.class, 1);
            System.out.println("I found the employee: " + employee);
        }
    }

    private static void insertSomeEmployees(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            Employee employee1 = new Employee();
            employee1.setFirstName("Brad");
            employee1.setLastName("Pitt");

            Employee employee2 = new Employee();
            employee2.setFirstName("George");
            employee2.setLastName("Clooney");

            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.persist(employee1);
                session.persist(employee2);
                transaction.commit();
            } catch (Exception e) {
                System.err.println("I couldn't insert the employees because: " + e.toString());
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        }
    }
}