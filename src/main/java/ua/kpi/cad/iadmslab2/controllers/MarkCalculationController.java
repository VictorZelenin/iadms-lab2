package ua.kpi.cad.iadmslab2.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.kpi.cad.iadmslab2.service.MarkCalculationService;

@Controller
@Slf4j
@RequestMapping("/calculate")
@RestController
public class MarkCalculationController {

    private MarkCalculationService calculationService;

    @Autowired
    public MarkCalculationController(MarkCalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @PostMapping("/{id}")
    public void calculateMark(@PathVariable Integer id) {

    }

    @GetMapping
    public String debug() {
        Double aDouble = calculationService.calculateMarkComplexity(3, 2, 0.2);

        return "SUCCESS";
    }
}
