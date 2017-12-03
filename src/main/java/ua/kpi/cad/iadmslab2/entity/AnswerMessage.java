package ua.kpi.cad.iadmslab2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AnswerMessage implements Serializable {
    private String studentName;
    private Integer questionId;
    private Double answerValue;
}
