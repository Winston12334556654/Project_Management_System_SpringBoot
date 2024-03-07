package ai.group2.project_management_system.service.Impl;

import ai.group2.project_management_system.dto.ProjectDTO;
import ai.group2.project_management_system.model.Enum.Status;
import ai.group2.project_management_system.model.entity.Project;
import ai.group2.project_management_system.model.entity.User;
import ai.group2.project_management_system.repository.ProjectRepository;
import ai.group2.project_management_system.repository.*;
import ai.group2.project_management_system.service.ProjectService;
import jakarta.persistence.Cacheable;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public  class ProjectServiceImpl implements ProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final EntityManager entityManager;

    @Override
    public Project save(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public List<Project> getAllProjectsWithUsers() {
        List<Project> projects = projectRepository.findAll();
        // Example: Fetch users for each project
        for (Project project : projects) {
            // Trigger lazy loading by accessing the users association
            Set<User> users = project.getUsers();
            // Optional: Print the users for demonstration
            users.forEach(user -> System.out.println("User: " + user.getName() + " " + user.getId()));
        }
        return projects;
    }


    @Override
    public Project getProjectBy_Id(Long projectId) {
       return projectRepository.findProjectById(projectId);
   }

       @Override
    public ProjectDTO getProjectById(Long id) {
        return new ProjectDTO(projectRepository.getReferenceById(id));
    }

    @Override
    public List<Long> getUserIdsByProjectId(Long projectId) {
        System.out.println("Welcome Service"+ projectId);
        String jpql = "SELECT u.id FROM Project p JOIN p.users u WHERE p.id = :projectId";

        return entityManager.createQuery(jpql, Long.class)
                .setParameter("projectId", projectId)
                .getResultList();
    }
    @Override
    public Project findById(Long projectId) {
        return projectRepository.findProjectById(projectId) ;
    }
    @Override
    public int getProjectCount() {
        return (int) projectRepository.count();
    }
    @Override
    public int getActiveProjectCount() {
        List<Project> allProjects = projectRepository.findAll();
        int activeProjects = 0;
        for (Project project : allProjects){
            if(project.isActive()){
                activeProjects++;
            }
        }
        return activeProjects;
    }
    @Override
    public int getInactiveProjectCount() {
        List<Project> allProjects = projectRepository.findAll();
        int inActiveProjects = 0;
        for (Project project : allProjects){
            if(!project.isActive()){
                inActiveProjects++;
            }
        }
        return inActiveProjects;
    }
    @Override
    public List<Project> getActiveProjects() {
        List<Project> allProjects = projectRepository.findAll();
        List<Project> activeProjects = new ArrayList<>();
        for (Project project : allProjects) {
            if (project.isActive()) {
                activeProjects.add(project);
            }
        }
        return activeProjects;
    }

    @Override
    public List<Project> getProjectsByDepartmentId(Long departmentId) {
        return null;
    }

    @Override
    public Map<String, Integer> getCountsByStatus() {
        Map<String, Integer> countsByStatus = new HashMap<>();
        countsByStatus.put("todo", projectRepository.countByStatus(Status.TODO));
        countsByStatus.put("inProgress", projectRepository.countByStatus(Status.INPROGRESS));
        countsByStatus.put("pending", projectRepository.countByStatus(Status.PENDING));
       /* countsByStatus.put("overdue", projectRepository.countByStatus(Status.OVERDUE));*/
        countsByStatus.put("completed", projectRepository.countByStatus(Status.COMPLETED));
        return countsByStatus;
    }


    @Override
    public List<Long> getAllProjectIds() {
        return projectRepository.findAllProjectIds();
    }
}
