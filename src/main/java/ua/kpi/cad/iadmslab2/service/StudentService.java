package ua.kpi.cad.iadmslab2.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kpi.cad.iadmslab2.entity.QuestionAnswer;
import ua.kpi.cad.iadmslab2.entity.Student;
import ua.kpi.cad.iadmslab2.entity.StudentAnswer;
import ua.kpi.cad.iadmslab2.repository.StudentRepository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StudentService {

    private StudentRepository studentRepository;
    private Map<String, Student> cache;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        cache = new HashMap<>();
    }

    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    public Student findStudentByName(String name) {
        Student student = cache.get(name);

        if (student == null) {
            student = studentRepository.findByName(name);
        }

        if (student == null) {
            student = studentRepository.save(new Student(name));
        }

        return student;
    }

    public Student saveStudentTotalMark(Map<StudentAnswer, Double> answersWithComplexities) {
        Double max = findSumOfBestAnswers(answersWithComplexities);
        Double answeredVal = getSumOfStudentAnswers(answersWithComplexities);
        Double totalMark = answeredVal / max;

        Student student = answersWithComplexities.keySet().iterator().next().getStudent();

        student.setTotalMark(totalMark);

        return studentRepository.save(student);
    }

    private Double findSumOfBestAnswers(Map<StudentAnswer, Double> answers) {
        return answers.entrySet().stream()
                .map(entry -> entry.getKey().getQuestion().getAnswers().stream()
                        .map(QuestionAnswer::getAnswerValue)
                        .max(Comparator.naturalOrder()).orElse(0.0) * entry.getValue())
                .mapToDouble(d -> d)
                .sum();
    }

    private Double getSumOfStudentAnswers(Map<StudentAnswer, Double> answers) {
        return answers.entrySet().stream()
                .map(entry -> entry.getValue() * entry.getKey().getAnswerValue())
                .mapToDouble(d -> d)
                .sum();
    }
}
