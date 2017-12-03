package ua.kpi.cad.iadmslab2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ua.kpi.cad.iadmslab2.entity.StudentAnswer;

import java.util.List;

public interface StudentAnswersRepository extends JpaRepository<StudentAnswer, Integer>, JpaSpecificationExecutor<StudentAnswer> {

    StudentAnswer findFirstByOrderByAnswerTimeDesc();

    List<StudentAnswer> findAllByPkQuestionIdOrderByAnswerTimeDesc(int id);
}
