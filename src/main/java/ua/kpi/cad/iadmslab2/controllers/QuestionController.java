package ua.kpi.cad.iadmslab2.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.kpi.cad.iadmslab2.entity.Question;
import ua.kpi.cad.iadmslab2.service.QuestionService;

import java.util.List;

@RestController
@RequestMapping("/questions")
@Slf4j
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public List<Question> getAllQuestions() {
        return questionService.findAllQuestions();
    }
}
