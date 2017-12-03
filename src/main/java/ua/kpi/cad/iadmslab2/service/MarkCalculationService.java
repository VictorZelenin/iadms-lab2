package ua.kpi.cad.iadmslab2.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kpi.cad.iadmslab2.entity.QuestionAnswer;
import ua.kpi.cad.iadmslab2.entity.StudentAnswer;
import ua.kpi.cad.iadmslab2.repository.QuestionRepository;
import ua.kpi.cad.iadmslab2.repository.StudentAnswersRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MarkCalculationService {

    private final StudentAnswersRepository answersRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public MarkCalculationService(StudentAnswersRepository answersRepository, QuestionRepository questionRepository) {
        this.answersRepository = answersRepository;
        this.questionRepository = questionRepository;
    }

    public Double calculateMarkComplexity(Integer studentId, Integer questionId, Double selectedValue) {
        List<StudentAnswer> answers = answersRepository.findAllByPkQuestionIdOrderByAnswerTimeDesc(questionId);
        long questionsCount = questionRepository.count();

        StudentAnswer answer = findStudentAnswer(answers, studentId);

        // save new answer. (student_id, question_id, new_complexity, selectedValue)

        return recalculateComplexity(answer, questionsCount);
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
        Optional<StudentAnswer> optionalAnswer = answers.stream().filter(answer -> !answer.getStudent().getId().equals(studentId)).findFirst();

        if (!optionalAnswer.isPresent()) {
            throw new IllegalStateException("Could not find appropriate answer");
        }

        return optionalAnswer.get();
    }
}
