package ua.kpi.cad.iadmslab2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.kpi.cad.iadmslab2.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
}
