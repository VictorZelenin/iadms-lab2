package ua.kpi.cad.iadmslab2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ua.kpi.cad.iadmslab2.service.QuestionService;

import java.util.Map;

@Controller
public class BaseController {

    private QuestionService service;

    @Autowired
    public BaseController(QuestionService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index(Map<String, Object> model) {
        model.put("questions", service.findAllQuestions());

        return "index";
    }
}
