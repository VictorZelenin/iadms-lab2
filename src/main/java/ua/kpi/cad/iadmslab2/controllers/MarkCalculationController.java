package ua.kpi.cad.iadmslab2.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import ua.kpi.cad.iadmslab2.entity.AnswerMessage;
import ua.kpi.cad.iadmslab2.entity.Student;
import ua.kpi.cad.iadmslab2.service.MarkCalculationService;
import ua.kpi.cad.iadmslab2.service.StudentService;

@RequestMapping("/calculate")
@RestController
@Slf4j
public class MarkCalculationController {

    private MarkCalculationService calculationService;
    private StudentService studentService;

    @Autowired
    public MarkCalculationController(MarkCalculationService calculationService, StudentService studentService) {
        this.calculationService = calculationService;
        this.studentService = studentService;
    }

    @MessageMapping("/test")
    @SendTo("/topic/test")
    public Double recalculateQuestionComplexity(AnswerMessage message) {
        Student student = studentService.findStudentByName(message.getStudentName());

        return calculationService.calculateMarkComplexity(student.getId(), message.getQuestionId(), message.getAnswerValue());
    }
}