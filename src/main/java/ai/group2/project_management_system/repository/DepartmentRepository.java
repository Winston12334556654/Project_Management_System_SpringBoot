package ai.group2.project_management_system.repository;

import ai.group2.project_management_system.model.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    // Find a department by its name
    Department findByName(String name);
}