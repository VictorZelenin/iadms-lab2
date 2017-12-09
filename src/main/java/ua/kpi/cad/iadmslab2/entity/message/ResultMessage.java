package ua.kpi.cad.iadmslab2.entity.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ResultMessage implements Serializable {
    private String studentName;
    private Map<Integer, Double> questionAnswers;
}