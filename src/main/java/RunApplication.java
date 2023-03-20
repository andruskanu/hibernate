import config.HibernateUtils;
import dao.EmployeeRepository;
import dao.ProjectRepository;
import model.Employee;
import model.Project;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Optional;
import java.util.Set;

public class RunApplication {

    public static void main(String[] args) {

        try (SessionFactory sessionFactory = HibernateUtils.getSessionFactory()) {
            insertSomeEmployees(sessionFactory);
            findEmployee(sessionFactory, 1);

            EmployeeRepository employeeRepository = new EmployeeRepository(sessionFactory);
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

            asignProjectToEmployees(sessionFactory, projectRepository);
        }
    }

    private static void asignProjectToEmployees(SessionFactory sessionFactory, ProjectRepository projectRepository) {
        try (Session session = sessionFactory.openSession()) {
            Employee employee = session.find(Employee.class, 2);
            System.out.println("I found the employee with id 2: " + employee);

            Optional<Project> optionalProjectById2 = projectRepository.findById(2);
            System.out.println("The project with id 2: " + optionalProjectById2);
            Optional<Project> optionalProjectById3 = projectRepository.findById(3);
            System.out.println("The project with id 3: " + optionalProjectById3);

            if(employee != null) {
                Set<Project> projects = employee.getProjects();
                if(optionalProjectById2.isPresent()) projects.add(optionalProjectById2.get());
                if(optionalProjectById3.isPresent()) projects.add(optionalProjectById3.get());
            }

            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.merge(employee);
                transaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
                transaction.rollback();
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
            employee1.setFirstName("Matthew");
            employee1.setLastName("Baltazar");

            Employee employee2 = new Employee();
            employee2.setFirstName("George");
            employee2.setLastName("Firmware");

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