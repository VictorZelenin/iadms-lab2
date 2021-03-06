package ua.kpi.cad.iadmslab2.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kpi.cad.iadmslab2.entity.*;
import ua.kpi.cad.iadmslab2.repository.QuestionRepository;
import ua.kpi.cad.iadmslab2.repository.StudentAnswersRepository;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Slf4j
public class QuestionComplexityCalculationService {

    private final StudentAnswersRepository answersRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionComplexityCalculationService(StudentAnswersRepository answersRepository, QuestionRepository questionRepository) {
        this.answersRepository = answersRepository;
        this.questionRepository = questionRepository;
    }

    @Transactional
    public Question calculateQuestionComplexity(Integer studentId, Integer questionId, Double selectedValue) {
        List<StudentAnswer> answers = answersRepository.findAllByPkQuestionIdOrderByAnswerTimeDesc(questionId);
        long questionsCount = questionRepository.count();

        StudentAnswer answer = findStudentAnswer(answers, studentId);

        Double questionComplexity = recalculateComplexity(answer, questionsCount);
        log.debug("New complexity of question {}: {}", answer.getPk().getQuestion().getDescription(), questionComplexity);

        saveStudentAnswer(studentId, answer.getQuestion(), questionComplexity, selectedValue);

        updateQuestionComplexity(answer.getQuestion(), questionComplexity);

        return answer.getQuestion();
    }

    public List<StudentAnswer> getStudentAnswers(Student student) {
        return answersRepository.findStudentAnswerByPkStudentId(student.getId());
    }

    private Double recalculateComplexity(StudentAnswer answer, long numberOfQuestions) {
        double oldComplexity = answer.getQuestionComplexity();
        double answerValueByPrevStudent = answer.getAnswerValue();
        double userTotalMark = answer.getStudent().getTotalMark();

        Double maxAnswerValue = answer.getQuestion().getAnswers().stream()
                .map(QuestionAnswer::getAnswerValue)
                .max(Comparator.naturalOrder()).orElse(1.0);
        Double minAnswerValue = answer.getQuestion().getAnswers().stream()
                .map(QuestionAnswer::getAnswerValue)
                .min(Comparator.naturalOrder()).orElse(0.0);

        return oldComplexity * (1.0 + 1.0 / numberOfQuestions * (maxAnswerValue - answerValueByPrevStudent) - (1.0 - userTotalMark) *
                (answerValueByPrevStudent - minAnswerValue));
    }

    private StudentAnswer findStudentAnswer(List<StudentAnswer> answers, Integer studentId) {
        Optional<StudentAnswer> optionalAnswer = answers.stream()
                .filter(answer -> !answer.getStudent().getId().equals(studentId))
                .findFirst();

        if (!optionalAnswer.isPresent()) {
            throw new IllegalStateException("Could not find appropriate answer");
        }

        return optionalAnswer.get();
    }

    private void saveStudentAnswer(Integer studentId, Question question, Double questionComplexity, Double selectedValue) {
        Student student = new Student(studentId);
        StudentQuestionId id = new StudentQuestionId(student, question);

        StudentAnswer studentAnswer = new StudentAnswer(id, questionComplexity, selectedValue, new Date());

        answersRepository.save(studentAnswer);
    }

    private void updateQuestionComplexity(Question question, Double complexity) {
        question.setComplexity(complexity);

        questionRepository.save(question);
    }
}
