package ua.kpi.cad.iadmslab2.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kpi.cad.iadmslab2.entity.Question;
import ua.kpi.cad.iadmslab2.repository.QuestionRepository;

import java.util.List;

@Service
@Slf4j
public class QuestionService {

    private QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Question findQuestionById(Integer id) {
        return questionRepository.findOne(id);
    }

    public List<Question> findAllQuestions() {
        return questionRepository.findAll();
    }
}
