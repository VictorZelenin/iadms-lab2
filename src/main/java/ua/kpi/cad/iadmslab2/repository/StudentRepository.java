package ua.kpi.cad.iadmslab2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.kpi.cad.iadmslab2.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
