package ua.kpi.cad.iadmslab2.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import ua.kpi.cad.iadmslab2.entity.Question;
import ua.kpi.cad.iadmslab2.entity.StudentAnswer;
import ua.kpi.cad.iadmslab2.entity.message.AnswerMessage;
import ua.kpi.cad.iadmslab2.entity.Student;
import ua.kpi.cad.iadmslab2.entity.message.ResultMessage;
import ua.kpi.cad.iadmslab2.service.QuestionComplexityCalculationService;
import ua.kpi.cad.iadmslab2.service.StudentService;

import java.util.List;


@RequestMapping("/calculate")
@RestController
@Slf4j
public class QuestionComplexityCalculationController {

    private QuestionComplexityCalculationService calculationService;
    private StudentService studentService;

    @Autowired
    public QuestionComplexityCalculationController(QuestionComplexityCalculationService calculationService,
                                                   StudentService studentService) {
        this.calculationService = calculationService;
        this.studentService = studentService;
    }

    @MessageMapping("/test")
    @SendTo("/topic/test")
    public Question recalculateQuestionComplexity(AnswerMessage message) {
        Student student = studentService.findStudentByName(message.getStudentName());

        return calculationService.calculateQuestionComplexity(student.getId(), message.getQuestionId(), message.getAnswerValue());
    }

    @MessageMapping("/result")
    @SendTo("/topic/result")
    public Double calculateTotalMarkForStudent(ResultMessage message) {
        Student student = studentService.findStudentByName(message.getStudentName());

        List<StudentAnswer> answers = calculationService.getStudentAnswers(student);

        return studentService.saveStudentTotalMark(answers).getTotalMark();
    }
}