package ua.kpi.cad.iadmslab2.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kpi.cad.iadmslab2.entity.QuestionAnswer;
import ua.kpi.cad.iadmslab2.entity.Student;
import ua.kpi.cad.iadmslab2.entity.StudentAnswer;
import ua.kpi.cad.iadmslab2.repository.StudentRepository;

import java.text.DecimalFormat;
import java.util.*;

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

    public Student saveStudentTotalMark(List<StudentAnswer> answers) {
        Double max = findSumOfBestAnswers(answers);
        Double answeredVal = getSumOfStudentAnswers(answers);
        Double totalMark = answeredVal / max;

        Student student = answers.get(0).getStudent();

        student.setTotalMark(formatDecimalWithPrecision(totalMark));

        return studentRepository.save(student);
    }

    private Double findSumOfBestAnswers(List<StudentAnswer> answers) {
        return answers.stream()
                .map(answer -> answer.getQuestion().getAnswers().stream()
                        .map(QuestionAnswer::getAnswerValue)
                        .max(Comparator.naturalOrder()).orElse(0.0) * answer.getQuestionComplexity())
                .mapToDouble(d -> d)
                .sum();
    }

    private Double getSumOfStudentAnswers(List<StudentAnswer> answers) {
        return answers.stream()
                .map(answer -> answer.getAnswerValue() * answer.getQuestion().getComplexity())
                .mapToDouble(d -> d)
                .sum();
    }

    private Double formatDecimalWithPrecision(Double d) {
        return Double.parseDouble(new DecimalFormat("#0.00").format(d));
    }
}
